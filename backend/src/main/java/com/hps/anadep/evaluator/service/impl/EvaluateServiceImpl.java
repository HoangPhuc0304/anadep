package com.hps.anadep.evaluator.service.impl;

import com.hps.anadep.analyzer.client.AnadepClient;
import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.analyzer.client.OsvClient;
import com.hps.anadep.analyzer.service.OsvService;
import com.hps.anadep.evaluator.enums.Severity;
import com.hps.anadep.evaluator.service.EvaluateService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.LibraryFix;
import com.hps.anadep.model.SummaryLibraryFix;
import com.hps.anadep.model.depgraph.Dependency;
import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.github.SecurityAdvisoryRequest;
import com.hps.anadep.model.github.SecurityAdvisoryResponse;
import com.hps.anadep.model.github.SecurityAdvisoryVulnerability;
import com.hps.anadep.model.osv.Affected;
import com.hps.anadep.model.osv.Event;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.osv.VulnerabilityOSVResponse;
import com.hps.anadep.model.response.*;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.repository.AuthTokenRepository;
import com.hps.anadep.repository.UserRepository;
import com.hps.anadep.security.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import us.springett.cvss.Cvss;
import us.springett.cvss.Score;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    private OsvService osvService;
    @Autowired
    private GithubClient githubClient;
    @Autowired
    private OsvClient osvClient;
    @Autowired
    private AnadepClient anadepClient;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthTokenRepository authTokenRepository;

    public static final String CVSS_V2 = "CVSS_V2";
    public static final String CVSS_V3 = "CVSS_V3";
    public static final String CVSS_V4 = "CVSS_V4";
    public static final String APPLY_TO_FIX = "Auto apply to fix";
    public static final String REFER_TO_FIX = "Refer to fix";
    private static final String NAME_FORMAT = "%s:%s";
    private static final String CVE_PREFIX = "CVE";

    public double getScore(String vector) {
        Cvss cvss = Cvss.fromVector(vector);
        Score score = cvss.calculateScore();
        return score.getBaseScore();
    }

    public String getSeverityRating(double score) {
        if (score == Severity.NONE.getMinimum()) {
            return Severity.NONE.getName();
        } else if (score >= Severity.LOW.getMinimum() && score < Severity.MEDIUM.getMinimum()) {
            return Severity.LOW.getName();
        } else if (score >= Severity.MEDIUM.getMinimum() && score < Severity.HIGH.getMinimum()) {
            return Severity.MEDIUM.getName();
        } else if (score >= Severity.HIGH.getMinimum() && score < Severity.CRITICAL.getMinimum()) {
            return Severity.HIGH.getName();
        } else {
            return Severity.CRITICAL.getName();
        }
    }

    @Override
    public com.hps.anadep.model.osv.Severity evaluate(String vector) {
        if (!StringUtils.hasText(vector)) {
            return null;
        }
        double score = getScore(vector);
        String prefix = vector.split("/")[0];
        String type = null;
        if (prefix.contains("2")) {
            type = CVSS_V2;
        } else if (prefix.contains("3")) {
            type = CVSS_V3;
        } else if (prefix.contains("4")) {
            type = CVSS_V4;
        }
        return new com.hps.anadep.model.osv.Severity(type, vector, getScore(vector), getSeverityRating(score));
    }

    @Override
    public FixResult autoFix(AnalysisUIResult analysisUIResult) {
        long start = System.currentTimeMillis();
        FixResult fixResult = new FixResult();
        List<LibraryFix> libraryFix = new ArrayList<>();

        Map<Library, Set<String>> libraryFixMap = new HashMap<>();
        Map<Library, Set<Double>> libraryScoreMap = new HashMap<>();
        for (LibraryScanUI libraryScanUI : analysisUIResult.getLibs()) {
            Library library = libraryScanUI.getInfo();

            Set<String> versions = libraryFixMap.getOrDefault(library, new HashSet<>());
            Set<Double> scores = libraryScoreMap.getOrDefault(library, new HashSet<>());

            if (StringUtils.hasText(libraryScanUI.getVuln().getFixed())) {
                versions.add(libraryScanUI.getVuln().getFixed());
            }
            if (!CollectionUtils.isEmpty(libraryScanUI.getVuln().getSeverity())) {
                scores.add(libraryScanUI.getVuln().getSeverity().get(0).getBaseScore());
            }

            libraryFixMap.put(library, versions);
            libraryScoreMap.put(library, scores);
        }

        for (Library library : libraryFixMap.keySet()) {
            Set<String> fixedVersions = libraryFixMap.get(library);
            String fixedVersionVal = null;
            if (!fixedVersions.isEmpty()) {
                if (fixedVersions.size() > 1) {
                    fixedVersionVal = getOneFix(library, fixedVersions).stream().toList().get(0);
                } else {
                    fixedVersionVal = fixedVersions.stream().toList().get(0);
                }
            }
            LibraryFix fix = new LibraryFix(library, fixedVersionVal, null);
            if (!CollectionUtils.isEmpty(libraryScoreMap.get(library))) {
                List<Double> scores = libraryScoreMap.get(library).stream().toList();
                if (!CollectionUtils.isEmpty(scores)) {
                    Double maxScore = Collections.max(scores);
                    scores = List.of(maxScore);
                }
                fix.setSeverity(Severity.getSeverityFromScore(scores.get(0)).getName());
            }
            libraryFix.add(fix);
        }

        fixResult.setLibs(libraryFix);
        fixResult.setEcosystem(analysisUIResult.getEcosystem());
        fixResult.setResponseTime(System.currentTimeMillis() - start);
        return fixResult;
    }

    @Override
    public SummaryFix applyFix(AnalysisUIResult analysisUIResult, ScanningResult scanningResult) {
        long start = System.currentTimeMillis();
        SummaryFix summaryFix = new SummaryFix();
        List<SummaryLibraryFix> summaryLibraryFix = new ArrayList<>();

        FixResult fixResult = autoFix(analysisUIResult);
        String rootProject = scanningResult.getProjectName();
        Map<String, Library> libMap = scanningResult.getLibraries().stream().collect(Collectors.toMap(
                l -> NAME_FORMAT.formatted(l.getName(), l.getVersion()),
                Function.identity()
        ));

        Map<String, Set<Library>> depMap = new HashMap<>();
        for (Dependency dependency : scanningResult.getDependencies()) {
            String name = dependency.getTo();
            Set<Library> libraries = depMap.getOrDefault(name, new HashSet<>());
            libraries.add(libMap.get(dependency.getFrom()));
            depMap.put(name, libraries);
        }


        for (LibraryFix libFix : fixResult.getLibs()) {
            String name = NAME_FORMAT.formatted(libFix.getName(), libFix.getCurrentVersion());
            if (depMap.containsKey(name) && depMap.get(name).contains(libMap.get(rootProject))) {
                summaryLibraryFix.add(new SummaryLibraryFix(libFix, depMap.get(name), APPLY_TO_FIX));
            } else {
                summaryLibraryFix.add(new SummaryLibraryFix(libFix, depMap.get(name), REFER_TO_FIX));
            }
        }

        summaryFix.setLibs(summaryLibraryFix);
        summaryFix.setEcosystem(fixResult.getEcosystem());
        summaryFix.setResponseTime(System.currentTimeMillis() - start);
        return summaryFix;
    }

    @Override
    public void advisory(Repo repo, AnalysisUIResult analysisUIResult, AppUser appUser) {
        createSecurityAdvisory(repo, analysisUIResult, OsvClient.class, appUser);
    }

    @Override
    public void advisoryV2(Repo repo, AnalysisUIResult analysisUIResult, AppUser appUser) {
        createSecurityAdvisory(repo, analysisUIResult, AnadepClient.class, appUser);
    }

    private Set<String> getOneFix(Library library, Set<String> versions) {
        ExecutorService executorService = Executors.newFixedThreadPool(versions.size());
        List<CompletableFuture<String>> completableFutures = versions.stream()
                .map(version -> CompletableFuture.supplyAsync(() -> {
                    try {
                        Library lib = new Library(library);
                        lib.setVersion(version);
                        VulnerabilityOSVResponse vulnerabilityOSVResponse = osvService.findVulnerabilities(lib);
                        if (vulnerabilityOSVResponse == null || CollectionUtils.isEmpty(vulnerabilityOSVResponse.getVulns())) {
                            return version;
                        }
                        return null;
                    } catch (Exception e) {
                        return null;
                    }
                }, executorService))
                .toList();
        Set<String> fixedVersionSet = new HashSet<>();
        List<String> fixedVersion = completableFutures.stream()
                .map(CompletableFuture::join)
                .toList();

        for (String version : fixedVersion) {
            if (StringUtils.hasText(version)) {
                fixedVersionSet.add(version);
            }
        }

        executorService.shutdown();
        return fixedVersionSet;
    }

    private void createSecurityAdvisory(Repo repo, AnalysisUIResult analysisUIResult, Class<?> classz, AppUser appUser) {
        try {
            User user = userRepository.findById(appUser.getId()).orElseThrow(
                    () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
            AuthToken authToken = authTokenRepository.findByUser(user).orElseThrow(
                    () -> new RuntimeException(String.format("The user with id [%s] doesn't have token", appUser.getId())));

            List<String> databaseIds = analysisUIResult.getLibs().stream().map(a -> a.getVuln().getId()).toList();
            ExecutorService executorService = Executors.newFixedThreadPool(databaseIds.size());
            List<CompletableFuture<Vulnerability>> completableFutures = databaseIds.stream()
                    .map(databaseId -> CompletableFuture.supplyAsync(() -> {
                        if (classz.isAssignableFrom(AnadepClient.class)) {
                            return anadepClient.getVulnerability(databaseId);
                        }
                        return osvClient.getVulnerability(databaseId);
                    }, executorService))
                    .toList();

            List<Vulnerability> vulns = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
            executorService.shutdown();

            List<SecurityAdvisoryRequest> securityAdvisoryRequests = new ArrayList<>();
            for (Vulnerability vuln : vulns) {
                SecurityAdvisoryRequest.SecurityAdvisoryRequestBuilder requestBuilder = SecurityAdvisoryRequest.builder()
                        .databaseId(vuln.getId())
                        .summary(vuln.getSummary())
                        .description(vuln.getDetails())
                        .cvssVector(CollectionUtils.isEmpty(vuln.getSeverity()) ? "" : vuln.getSeverity().get(0).getScore())
                        .vulnerabilities(createSecurityAdvisoryVulnerability(vuln));

                if (!CollectionUtils.isEmpty(vuln.getAliases())) {
                    Optional<String> cve = vuln.getAliases().stream().filter(a -> a.toUpperCase().startsWith(CVE_PREFIX)).findFirst();
                    if (cve.isPresent()) {
                        requestBuilder.cveId(cve.get());
                    }
                }
                SecurityAdvisoryRequest request = requestBuilder.build();
                securityAdvisoryRequests.add(request);
            }

            SecurityAdvisoryResponse[] advisoryResponses = githubClient.getSecurityAdvisories(repo.getFullName(), authToken.getGithubToken());
            Map<String, SecurityAdvisoryResponse> advisoryMap = new HashMap<>();
            for (SecurityAdvisoryResponse response : advisoryResponses) {
                String cve = StringUtils.hasText(response.getCveId()) ? response.getCveId() : "";
                String name = NAME_FORMAT.formatted(cve, response.getSummary());
                advisoryMap.put(name, response);
            }

            Map<String, SecurityAdvisoryRequest> requestMap = new HashMap<>();
            for (SecurityAdvisoryRequest request : securityAdvisoryRequests) {
                String cve = StringUtils.hasText(request.getCveId()) ? request.getCveId() : "";
                String name = NAME_FORMAT.formatted(cve, request.getSummary());
                requestMap.put(name, request);
            }

            for (Map.Entry<String, SecurityAdvisoryRequest> entry : requestMap.entrySet()) {
                if (advisoryMap.containsKey(entry.getKey())) {
                    SecurityAdvisoryResponse response = advisoryMap.get(entry.getKey());
                    log.info("Update vulnerability to repo {} with id: {}", repo.getFullName(), entry.getValue().getDatabaseId());
                    githubClient.updateSecurityAdvisory(repo.getFullName(), response.getGhsaId(), entry.getValue(), authToken.getGithubToken());
                } else {
                    log.info("Add vulnerability to repo {} with id: {}", repo.getFullName(), entry.getValue().getDatabaseId());
                    githubClient.createSecurityAdvisory(repo.getFullName(), entry.getValue(), authToken.getGithubToken());
                }
            }
        } catch (Exception e) {
            log.error("Creating Security Advisory failed with message: {}", e.getMessage());
        }

    }

    private List<SecurityAdvisoryVulnerability> createSecurityAdvisoryVulnerability(Vulnerability vuln) {
        List<SecurityAdvisoryVulnerability> securityAdvisoryVulnerabilities = new ArrayList<>();
        for(Affected affected : vuln.getAffected()) {
            String name = affected.getLibraryEcosystem().getName();
            Ecosystem eco = Ecosystem.getEcosystemByOsvName(affected.getLibraryEcosystem().getEcosystem());
            if (eco == null) {
                continue;
            }
            String ecosystem = eco.getGithubName();

            List<String> versions = affected.getVersions();
            List<Event> events = affected.getRanges().get(0).getEvents();
            Optional<Event> introducedEvent = events.stream().filter(e -> StringUtils.hasText(e.getIntroduced())).findFirst();
            Optional<Event> lastAffectedEvent = events.stream().filter(e -> StringUtils.hasText(e.getLastAffected())).findFirst();
            Optional<Event> fixedEvent = events.stream().filter(e -> StringUtils.hasText(e.getFixed())).findFirst();

            String versionRange = "";
            if (introducedEvent.isEmpty()) {
                if (lastAffectedEvent.isPresent()) {
                    versionRange = "<= %s".formatted(lastAffectedEvent.get().getLastAffected());
                } else if (fixedEvent.isPresent()) {
                    versionRange = "< %s".formatted(fixedEvent.get().getFixed());
                } else if (!CollectionUtils.isEmpty(versions)) {
                    versionRange = String.join(", ", versions);
                }
            } else {
                if (lastAffectedEvent.isPresent()) {
                    versionRange = ">= %s, <= %s".formatted(introducedEvent.get().getIntroduced(), lastAffectedEvent.get().getLastAffected());
                } else if (fixedEvent.isPresent()) {
                    versionRange = ">= %s, < %s".formatted(introducedEvent.get().getIntroduced(), fixedEvent.get().getFixed());
                } else {
                    versionRange = ">= %s".formatted(introducedEvent.get().getIntroduced());
                }
            }

            securityAdvisoryVulnerabilities.add(SecurityAdvisoryVulnerability.builder()
                    .library(new Library(name, null, ecosystem))
                    .vulnerableVersionRange(versionRange)
                    .patchedVersions(fixedEvent.isPresent() ? fixedEvent.get().getFixed() : "")
                    .build());
        }

        return securityAdvisoryVulnerabilities;
    }
}

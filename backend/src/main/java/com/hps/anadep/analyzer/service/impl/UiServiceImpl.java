package com.hps.anadep.analyzer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.analyzer.client.AnadepClient;
import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.analyzer.client.OsvClient;
import com.hps.anadep.analyzer.service.UiService;
import com.hps.anadep.evaluator.enums.Severity;
import com.hps.anadep.exception.NotFoundException;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.History;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.github.SecurityAdvisoryRequest;
import com.hps.anadep.model.github.SecurityAdvisoryVulnerability;
import com.hps.anadep.model.osv.Affected;
import com.hps.anadep.model.osv.Event;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.LibraryScan;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.VulnerabilityResponse;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.model.ui.VulnerabilitySummary;
import com.hps.anadep.repository.AuthTokenRepository;
import com.hps.anadep.repository.HistoryRepository;
import com.hps.anadep.repository.RepoRepository;
import com.hps.anadep.repository.UserRepository;
import com.hps.anadep.security.AppUser;
import com.hps.anadep.service.AppService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UiServiceImpl implements UiService {
    @Autowired
    private AppService appService;
    @Autowired
    private GithubClient githubClient;
    @Autowired
    private OsvClient osvClient;
    @Autowired
    private AnadepClient anadepClient;
    @Autowired
    private RepoRepository repoRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthTokenRepository authTokenRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public AnalysisUIResult retrieve(Library library) {
        AnalysisResult analysisResult = appService.retrieve(library);
        return getAnalysisUIResult(analysisResult);
    }

    @Override
    public AnalysisUIResult retrieveV2(Library library) {
        AnalysisResult analysisResult = appService.retrieveV2(library);
        return getAnalysisUIResult(analysisResult);
    }

    @Override
    public ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception {
        return appService.scan(file, includeTransitive);
    }

    @Override
    @Transactional
    public ScanningResult scan(String repoId, MultipartFile file, boolean includeTransitive, AppUser appUser) throws Exception {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new NotFoundException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);

        ScanningResult scanningResult = appService.scan(file, includeTransitive);
        History history = historyRepository.save(
                History.builder()
                        .scanningResult(objectMapper.writeValueAsString(scanningResult))
                        .type(ReportType.SBOM.name())
                        .createdAt(new Date())
                        .repo(repo).build());
        historyRepository.save(history);
        return scanningResult;
    }

    @Override
    public AnalysisUIResult analyze(MultipartFile file, boolean includeSafe) throws Exception {
        AnalysisResult analysisResult = appService.analyze(file, includeSafe);
        return appService.reformat(analysisResult);
    }

    @Override
    @Transactional
    public AnalysisUIResult analyze(String repoId, MultipartFile file, boolean includeSafe, AppUser appUser) throws Exception {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new NotFoundException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);

        History history = new History();
        history.setType(ReportType.VULNS.name());
        history.setCreatedAt(new Date());
        history.setRepo(repo);

        AnalysisResult analysisResult = appService.analyze(file, includeSafe, history);
        AnalysisUIResult analysisUIResult = appService.reformat(analysisResult);
        history.setVulnerabilityResult(objectMapper.writeValueAsString(analysisUIResult));
        historyRepository.save(history);
        return analysisUIResult;
    }

    @Override
    public AnalysisUIResult analyzeV2(MultipartFile file, boolean includeSafe) throws Exception {
        AnalysisResult analysisResult = appService.analyzeV2(file, includeSafe);
        return appService.reformat(analysisResult);
    }

    @Override
    @Transactional
    public AnalysisUIResult analyzeV2(String repoId, MultipartFile file, boolean includeSafe, AppUser appUser) throws Exception {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new NotFoundException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);

        History history = new History();
        history.setType(ReportType.VULNS.name());
        history.setCreatedAt(new Date());
        history.setRepo(repo);

        AnalysisResult analysisResult = appService.analyzeV2(file, includeSafe, history);
        AnalysisUIResult analysisUIResult = appService.reformat(analysisResult);
        history.setVulnerabilityResult(objectMapper.writeValueAsString(analysisUIResult));
        historyRepository.save(history);
        return analysisUIResult;
    }

    @Override
    public byte[] repoDownload(String url, String accessToken) {
        String repo = getRepoFromGithubUrl(url);
        return githubClient.download(repo, accessToken);
    }

    @Override
    public Vulnerability getVulnById(String id) {
        return osvClient.getVulnerability(id);
    }

    @Override
    public Vulnerability getVulnByIdV2(String id) {
        return anadepClient.getVulnerability(id);
    }

    @Override
    public VulnerabilitySummary summary(AnalysisUIResult analysisUIResult) {
        VulnerabilitySummary vulnerabilitySummary = new VulnerabilitySummary();
        if (CollectionUtils.isEmpty(analysisUIResult.getLibs())) {
            return vulnerabilitySummary;
        }
        analysisUIResult.getLibs().forEach(result -> {
            List<com.hps.anadep.model.osv.Severity> severities = result.getVuln().getSeverity();
            if (!CollectionUtils.isEmpty(severities)) {
                Severity severity = Severity.getSeverityFromName(result.getVuln().getSeverity().get(0).getRanking());
                switch (severity) {
                    case LOW -> vulnerabilitySummary.setLow(vulnerabilitySummary.getLow() + 1);
                    case MEDIUM -> vulnerabilitySummary.setMedium(vulnerabilitySummary.getMedium() + 1);
                    case HIGH -> vulnerabilitySummary.setHigh(vulnerabilitySummary.getHigh() + 1);
                    case CRITICAL -> vulnerabilitySummary.setCritical(vulnerabilitySummary.getCritical() + 1);
                    default -> vulnerabilitySummary.setNone(vulnerabilitySummary.getNone() + 1);
                }
            }
        });
        return vulnerabilitySummary;
    }

    private AnalysisUIResult getAnalysisUIResult(AnalysisResult analysisResult) {
        List<LibraryScanUI> libs = new ArrayList<>();
        for (LibraryScan libScan : analysisResult.getLibs()) {
            Library info = new Library(libScan.getInfo().getName(), libScan.getInfo().getVersion(), libScan.getInfo().getEcosystem());
            for (VulnerabilityResponse vulRes : libScan.getVulns()) {
                libs.add(new LibraryScanUI(info, vulRes));
            }
        }
        return new AnalysisUIResult(
                libs,
                analysisResult.getEcosystem(),
                analysisResult.getIssuesCount(),
                analysisResult.getLibraryCount(),
                analysisResult.isIncludeSafe(),
                analysisResult.getResponseTime()
        );
    }

    private String getRepoFromGithubUrl(String url) {
        String repo = url;
        if (url.endsWith(".git")) {
            repo = url.substring(0, url.length() - 4);
        }
        int index = repo.lastIndexOf("/");
        return repo.substring(repo.lastIndexOf("/", index - 1) + 1);
    }

    private void validateUserId(UUID id, AppUser appUser) {
        if (!appUser.getId().equals(id)) {
            throw new RuntimeException("Authorize failed");
        }
    }
}

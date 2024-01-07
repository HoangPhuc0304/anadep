package com.hps.osvscanning.service.impl;

import com.hps.osvscanning.model.*;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Ecosystem;
import com.hps.osvscanning.model.mvn.ArtifactDoc;
import com.hps.osvscanning.model.mvn.LibraryBulk;
import com.hps.osvscanning.model.osv.*;
import com.hps.osvscanning.model.response.LibraryScan;
import com.hps.osvscanning.model.response.ResponseResult;
import com.hps.osvscanning.model.response.VulnerabilityResponse;
import com.hps.osvscanning.service.FileService;
import com.hps.osvscanning.service.MavenService;
import com.hps.osvscanning.service.OsvService;
import com.hps.osvscanning.service.ScanService;
import com.hps.osvscanning.util.MavenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class ScanServiceImpl implements ScanService {
    @Autowired
    private MavenService mavenService;
    @Autowired
    private OsvService osvService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MavenTool mavenTool;
    @Value("${library.custom-fix.enable}")
    private Boolean enabledCustomFix;
    @Value("${library.available-fix.enable}")
    private Boolean enabledAvailableFix;
    private static final int MAX_LIBRARY_RECORDS = 200;

    @Override
    public ResponseResult retrieve(Library libraryInfo) {
        ResponseResult responseResult = new ResponseResult();
        long start = System.currentTimeMillis();
        VulnerabilityOSVResponse vulnerabilityOSVResponse = osvService.findVulnerabilities(libraryInfo);
        LibraryScan libraryScan = refactor(vulnerabilityOSVResponse, libraryInfo);
        if (CollectionUtils.isEmpty(libraryScan.getVulns())) {
            return new ResponseResult(List.of(libraryScan), Ecosystem.MAVEN.name(), 1, true, System.currentTimeMillis() - start);
        }

        responseResult.setLibs(List.of(libraryScan));
        responseResult.setEcosystem(Ecosystem.MAVEN.name());
        responseResult.setIssuesCount(1);
        responseResult.setIncludeSafe(true);
        responseResult.setLibraryCount(1);
        responseResult.setResponseTime(System.currentTimeMillis() - start);

        return responseResult;
    }

    @Override
    public ResponseResult scan(MultipartFile file, Boolean includeSafe) {
        ResponseResult responseResult = new ResponseResult();
        List<LibraryScan> libraryScans = new ArrayList<>();
        Set<Library> libraries = Collections.emptySet();
        long start = System.currentTimeMillis();
        int issuesCount = 0;
        try {
            fileService.save(file);
            libraries = mavenTool.getDependencies(true);

            ExecutorService executorService = Executors.newFixedThreadPool(libraries.size());
            List<CompletableFuture<ResponseResult>> completableFutures = libraries.stream()
                    .map(library -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return retrieve(library);
                        } catch (Exception e) {
                            LibraryScan libraryScan = refactor((VulnerabilityOSVResponse) null, library);
                            return new ResponseResult(List.of(libraryScan), Ecosystem.MAVEN.name(), 1, includeSafe, System.currentTimeMillis() - start);
                        }
                    }))
                    .toList();

            List<ResponseResult> responseResults = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            for (ResponseResult res : responseResults) {
                if (res.getIssuesCount() == 1) {
                    issuesCount += 1;
                } else if (!includeSafe) {
                    continue;
                }

                libraryScans.addAll(res.getLibs());
            }

            executorService.shutdown();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            fileService.clean();
        }

        responseResult.setLibs(libraryScans);
        responseResult.setEcosystem(Ecosystem.MAVEN.name());
        responseResult.setLibraryCount(libraries.size());
        responseResult.setIssuesCount(issuesCount);
        responseResult.setResponseTime(System.currentTimeMillis() - start);

        return responseResult;
    }

    private LibraryScan refactor(VulnerabilityOSVResponse vulnerabilityOSVResponse, Library libraryInfo) {
        if (vulnerabilityOSVResponse == null || CollectionUtils.isEmpty(vulnerabilityOSVResponse.getVulns())) {
            return new LibraryScan(libraryInfo, Collections.emptyList());
        }
        LibraryScan libraryScan = new LibraryScan();
        libraryScan.setInfo(libraryInfo);

        ExecutorService executorService = Executors.newFixedThreadPool(vulnerabilityOSVResponse.getVulns().size());
        List<CompletableFuture<VulnerabilityResponse>> completableFutures = vulnerabilityOSVResponse.getVulns().stream()
                .map(vuln -> CompletableFuture.supplyAsync(() -> refactor(vuln, libraryInfo)))
                .toList();

        List<VulnerabilityResponse> vulns = completableFutures.stream()
                .map(CompletableFuture::join)
                .toList();

        libraryScan.setVulns(vulns);
        executorService.shutdown();
        return libraryScan;
    }

    private VulnerabilityResponse refactor(Vulnerability vulnerability, Library libraryInfo) {
        VulnerabilityResponse vuln = new VulnerabilityResponse();
        BeanUtils.copyProperties(vulnerability, vuln);
        Fix fix = findFix(vulnerability, libraryInfo);
        if (fix != null) {
            vuln.setFixed(fix.getLibraryInfo().getVersion());
        }
        return vuln;
    }

    private Fix findFix(Vulnerability vul, Library libraryInfo) {
        Set<String> effectedVersions = new HashSet<>();
        Set<String> effectedVersionsInRange = new HashSet<>();
        for (Affected affected : vul.getAffected()) {
            if (!affected.getLibraryEcosystem().getName().equals(libraryInfo.getLibraryShortName()) ||
                    CollectionUtils.isEmpty(affected.getVersions())) {
                continue;
            }
            effectedVersions.addAll(affected.getVersions());
            if (!affected.getVersions().contains(libraryInfo.getVersion())) {
                continue;
            }
            effectedVersionsInRange.addAll(affected.getVersions());

            if (!enabledAvailableFix) {
                continue;
            }
            for (Range range : affected.getRanges()) {
                for (Event event : range.getEvents()) {
                    if (StringUtils.hasText(event.getFixed())) {
                        return new Fix(
                                new Library(
                                        libraryInfo.getGroupId(),
                                        libraryInfo.getArtifactId(),
                                        event.getFixed()
                                )
                        );
                    }
                }
            }
        }
        if (enabledCustomFix) {
            return findFixByCustom(libraryInfo, effectedVersions, effectedVersionsInRange);
//            if (fix != null) {
//                VulnerabilityList vulnerabilityList = osvService.findVulnerabilities(fix.getLibraryInfo());
//                if (vulnerabilityList.getVulns() == null) {
//                    return fix;
//                }
//            }
        }
        return null;
    }

    private Fix findFixByCustom(Library libraryInfo, Set<String> versions, Set<String> versionsInRange) {
        int allVersions = mavenService.getLibraryCount(libraryInfo);
        if (allVersions > MAX_LIBRARY_RECORDS) {
            allVersions = MAX_LIBRARY_RECORDS;
        }
        LibraryBulk libraryBulk = mavenService.findLibrary(libraryInfo, allVersions);
        List<ArtifactDoc> docs = libraryBulk.getResponse().getDocs();
        if (CollectionUtils.isEmpty(docs)) {
            return null;
        }

        int idx = 0;
        for (int i = docs.size() - 1; i >= 0; i--) {
            versionsInRange.remove(docs.get(i).getV());
            if (versionsInRange.isEmpty()) {
                idx = i;
                break;
            }
        }

        Fix defaultFix = new Fix(
                new Library(
                        libraryInfo.getGroupId(),
                        libraryInfo.getArtifactId(),
                        docs.get(0).getV()
                )
        );

        for (int i = idx - 1; i >= 0; i--) {
            if (!versions.contains(docs.get(i).getV())) {
                return new Fix(
                        new Library(
                                libraryInfo.getGroupId(),
                                libraryInfo.getArtifactId(),
                                docs.get(i).getV()
                        )
                );
            }
        }
        return defaultFix;
    }
}

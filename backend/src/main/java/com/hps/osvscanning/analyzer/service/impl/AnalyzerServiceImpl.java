package com.hps.osvscanning.analyzer.service.impl;

import com.hps.osvscanning.analyzer.service.FixService;
import com.hps.osvscanning.evaluator.service.EvaluateService;
import com.hps.osvscanning.model.Fix;
import com.hps.osvscanning.analyzer.service.AnalyzerService;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Ecosystem;
import com.hps.osvscanning.model.osv.*;
import com.hps.osvscanning.model.response.LibraryScan;
import com.hps.osvscanning.model.response.AnalysisResult;
import com.hps.osvscanning.model.response.VulnerabilityResponse;
import com.hps.osvscanning.analyzer.service.OsvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AnalyzerServiceImpl implements AnalyzerService {
    @Autowired
    private OsvService osvService;
    @Autowired
    private FixService fixService;
    @Autowired
    private EvaluateService evaluateService;

    @Override
    public AnalysisResult analyze(Library library) {
        AnalysisResult analysisResult = new AnalysisResult();
        long start = System.currentTimeMillis();
        VulnerabilityOSVResponse vulnerabilityOSVResponse = osvService.findVulnerabilities(library);
        LibraryScan libraryScan = refactor(vulnerabilityOSVResponse, library);
        if (CollectionUtils.isEmpty(libraryScan.getVulns())) {
            return new AnalysisResult(List.of(libraryScan), library.getEcosystem(), 1, true, System.currentTimeMillis() - start);
        }

        analysisResult.setLibs(List.of(libraryScan));
        analysisResult.setEcosystem(library.getEcosystem());
        analysisResult.setIssuesCount(1);
        analysisResult.setIncludeSafe(true);
        analysisResult.setLibraryCount(1);
        analysisResult.setResponseTime(System.currentTimeMillis() - start);

        return analysisResult;
    }

    @Override
    public AnalysisResult analyze(Set<Library> libraries, Boolean includeSafe) {
        AnalysisResult analysisResult = new AnalysisResult();
        List<LibraryScan> libraryScans = new ArrayList<>();
        Ecosystem ecosystem;

        if (CollectionUtils.isEmpty(libraries)) {
            return new AnalysisResult(Collections.emptyList(), null, 0, 0, includeSafe, 0);
        } else {
            ecosystem = Ecosystem.getEcosystem(libraries.iterator().next().getEcosystem());
        }

        long start = System.currentTimeMillis();
        int issuesCount = 0;
        try {
//            fileService.save(file);
//            libraries = mavenTool.getDependencies(true);

            ExecutorService executorService = Executors.newFixedThreadPool(libraries.size());
            List<CompletableFuture<AnalysisResult>> completableFutures = libraries.stream()
                    .map(library -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return analyze(library);
                        } catch (Exception e) {
                            LibraryScan libraryScan = refactor((VulnerabilityOSVResponse) null, library);
                            return new AnalysisResult(List.of(libraryScan), ecosystem.getOsvName(), 1, includeSafe, System.currentTimeMillis() - start);
                        }
                    }))
                    .toList();

            List<AnalysisResult> analysisResults = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();

            for (AnalysisResult res : analysisResults) {
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
        }

        analysisResult.setLibs(libraryScans);
        analysisResult.setEcosystem(ecosystem.getOsvName());
        analysisResult.setLibraryCount(libraries.size());
        analysisResult.setIssuesCount(issuesCount);
        analysisResult.setIncludeSafe(includeSafe);
        analysisResult.setResponseTime(System.currentTimeMillis() - start);

        return analysisResult;
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

        //Get score & ranking severity
        List<Severity> severities = vuln.getSeverity();
        if (severities == null) {
            severities = new ArrayList<>();
        }
        if (!CollectionUtils.isEmpty(severities)) {
            severities.forEach(severity -> {
                severity.setBaseScore(evaluateService.getScore(severity.getScore()));
                severity.setRanking(evaluateService.getSeverityRating(severity.getBaseScore()));
            });
        }
        vuln.setSeverity(severities);

        //Recommend to fix
        Fix fix = fixService.findFix(vulnerability, libraryInfo);
        if (fix != null) {
            vuln.setFixed(fix.getLibraryInfo().getVersion());
        }
        return vuln;
    }
}

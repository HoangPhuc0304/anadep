package com.hps.anadep.evaluator.service.impl;

import com.hps.anadep.analyzer.service.OsvService;
import com.hps.anadep.evaluator.enums.Severity;
import com.hps.anadep.evaluator.service.EvaluateService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.LibraryFix;
import com.hps.anadep.model.SummaryLibraryFix;
import com.hps.anadep.model.osv.VulnerabilityOSVResponse;
import com.hps.anadep.model.response.*;
import com.hps.anadep.scanner.service.ScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import us.springett.cvss.Cvss;
import us.springett.cvss.Score;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    private OsvService osvService;
    @Autowired
    private ScannerService scannerService;

    private final String CVSS_V2 = "CVSS_V2";
    private final String CVSS_V3 = "CVSS_V3";
    private final String CVSS_V4 = "CVSS_V4";
    private final String APPLY_TO_FIX = "Auto apply to fix";
    private final String REFER_TO_FIX = "Refer to fix";

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
    public FixResult autoFix(AnalysisResult analysisResult) {
        long start = System.currentTimeMillis();
        FixResult fixResult = new FixResult();
        List<LibraryFix> libraryFix = new ArrayList<>();
        Map<Library, Set<String>> libraryFixMap = new HashMap<>();
        for (LibraryScan libraryScan : analysisResult.getLibs()) {
            Set<String> fixedVersion = new HashSet<>();
            for (VulnerabilityResponse vuln : libraryScan.getVulns()) {
                if (StringUtils.hasText(vuln.getFixed())) {
                    fixedVersion.add(vuln.getFixed());
                }
            }
            if (fixedVersion.size() > 1) {
                fixedVersion = getOneFix(libraryScan.getInfo(), fixedVersion);
            }
            libraryFixMap.put(libraryScan.getInfo(), fixedVersion);
        }

        for (Library library : libraryFixMap.keySet()) {
            List<String> fixedVersionList = libraryFixMap.get(library).stream().toList();
            String fixedVersionVal = null;
            if (!fixedVersionList.isEmpty()) {
                fixedVersionVal = fixedVersionList.get(0);
            }
            libraryFix.add(new LibraryFix(library, fixedVersionVal));
        }

        fixResult.setLibs(libraryFix);
        fixResult.setEcosystem(analysisResult.getEcosystem());
        fixResult.setResponseTime(System.currentTimeMillis() - start);
        return fixResult;
    }

    @Override
    public SummaryFix applyFix(FixResult fixResult, MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        SummaryFix summaryFix = new SummaryFix();
        List<SummaryLibraryFix> summaryLibraryFix = new ArrayList<>();
        Map<Library, String> scannedLibMap = scannerService.scanToMap(file);

        for (LibraryFix libFix : fixResult.getLibs()) {
            Library lib = new Library(libFix.getName(), libFix.getCurrentVersion(), libFix.getEcosystem());
            if (!CollectionUtils.isEmpty(scannedLibMap) && scannedLibMap.containsKey(lib)) {
                summaryLibraryFix.add(new SummaryLibraryFix(libFix, new HashSet<>(), APPLY_TO_FIX));
            } else {
                Set<Library> usedBy = new HashSet<>();
                for (Map.Entry<Library, String> entry : scannedLibMap.entrySet()) {
                    if (scannerService.isUseLibrary(entry, lib)) {
                        usedBy.add(entry.getKey());
                    }
                }
                summaryLibraryFix.add(new SummaryLibraryFix(libFix, usedBy, REFER_TO_FIX));
            }
        }

        summaryFix.setLibs(summaryLibraryFix);
        summaryFix.setEcosystem(fixResult.getEcosystem());
        summaryFix.setResponseTime(System.currentTimeMillis() - start);
        return summaryFix;
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
                }))
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
}

package com.hps.anadep.service.impl;

import com.hps.anadep.analyzer.service.AnalyzerService;
import com.hps.anadep.evaluator.service.EvaluateService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.report.ReportRequest;
import com.hps.anadep.model.enums.Compress;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.*;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.reporter.service.ReportService;
import com.hps.anadep.scanner.service.ScannerService;
import com.hps.anadep.service.AppService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AppServiceImpl implements AppService {
    @Autowired
    private ScannerService scannerService;
    @Autowired
    private AnalyzerService analyzerService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ReportService reportService;

    @Override
    public AnalysisResult retrieve(Library library) {
        return analyzerService.analyze(library);
    }

    @Override
    public AnalysisResult retrieveV2(Library library) {
        return analyzerService.analyzeV2(library);
    }

    @Override
    public ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception {
        long start = System.currentTimeMillis();
        Set<Library> libraries = scannerService.scan(file, includeTransitive);
        String ecosystem = null;
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        if (extension.equals(Compress.ZIP.getName())) {
            Optional<Library> library = libraries.stream().findFirst();
            if (library.isPresent()) {
                ecosystem = library.get().getEcosystem();
            }
        } else {
            ecosystem = Ecosystem.getEcosystemFromPackageManagementFile(file.getOriginalFilename()).getOsvName();
        }

        return new ScanningResult(
                libraries,
                ecosystem,
                libraries.size(),
                includeTransitive,
                System.currentTimeMillis() - start
        );
    }

    @Override
    public AnalysisResult analyze(MultipartFile file, Boolean includeSafe) throws Exception {
        long start = System.currentTimeMillis();
        Set<Library> libraries = scannerService.scan(file, true);
        long scanningTime = System.currentTimeMillis() - start;
        AnalysisResult analysisResult = analyzerService.analyze(libraries, includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime() + scanningTime);
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeV2(MultipartFile file, Boolean includeSafe) throws Exception {
        long start = System.currentTimeMillis();
        Set<Library> libraries = scannerService.scan(file, true);
        long scanningTime = System.currentTimeMillis() - start;
        AnalysisResult analysisResult = analyzerService.analyzeV2(libraries, includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime() + scanningTime);
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeFast(ScanningResult scanningResult, Boolean includeSafe) throws Exception {
        Set<Library> libraries = scanningResult.getLibraries();
        AnalysisResult analysisResult = analyzerService.analyze(libraries, includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime());
        return analysisResult;
    }

    @Override
    public AnalysisResult analyzeFastV2(ScanningResult scanningResult, Boolean includeSafe) throws Exception {
        Set<Library> libraries = scanningResult.getLibraries();
        AnalysisResult analysisResult = analyzerService.analyzeV2(libraries, includeSafe);
        analysisResult.setResponseTime(analysisResult.getResponseTime());
        return analysisResult;
    }

    @Override
    public Severity evaluate(String vector) {
        return evaluateService.evaluate(vector);
    }

    @Override
    public void export(ReportRequest reportRequest, String projectName, String author, String type, String format, HttpServletResponse response) throws Exception {
        reportService.export(reportRequest.getData(), projectName, author, type, format, response);
    }

    @Override
    public AnalysisUIResult reformat(AnalysisResult analysisResult) {
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

    @Override
    public FixResult autoFix(AnalysisResult analysisResult) {
        return evaluateService.autoFix(analysisResult);
    }

    @Override
    public SummaryFix applyFix(FixResult fixResult, MultipartFile file) throws Exception {
        return evaluateService.applyFix(fixResult, file);
    }
}

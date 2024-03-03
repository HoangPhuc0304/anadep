package com.hps.osvscanning.service.impl;

import com.hps.osvscanning.analyzer.service.AnalyzerService;
import com.hps.osvscanning.evaluator.service.EvaluateService;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.ui.LibraryUI;
import com.hps.osvscanning.model.enums.Compress;
import com.hps.osvscanning.model.enums.Ecosystem;
import com.hps.osvscanning.model.osv.Severity;
import com.hps.osvscanning.model.response.*;
import com.hps.osvscanning.model.ui.AnalysisUIResult;
import com.hps.osvscanning.model.ui.LibraryScanUI;
import com.hps.osvscanning.scanner.service.ScannerService;
import com.hps.osvscanning.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Override
    public AnalysisResult retrieve(Library library) {
        return analyzerService.analyze(library);
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
    public Severity evaluate(String vector) {
        return evaluateService.evaluate(vector);
    }

    @Override
    public FixResult autoFix(AnalysisResult analysisResult) {
        return evaluateService.autoFix(analysisResult);
    }
}

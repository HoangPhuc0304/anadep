package com.hps.osvscanning.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.Severity;
import com.hps.osvscanning.model.response.AnalysisResult;
import com.hps.osvscanning.model.ui.AnalysisUIResult;
import com.hps.osvscanning.model.response.FixResult;
import com.hps.osvscanning.model.response.ScanningResult;
import org.springframework.web.multipart.MultipartFile;

public interface AppService {
    AnalysisResult retrieve(Library libraryInfo);
    ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception;
    AnalysisResult analyze(MultipartFile file, Boolean includeSafe) throws Exception;
    Severity evaluate(String vector);
    FixResult autoFix(AnalysisResult analysisResult);
}

package com.hps.anadep.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.report.ReportRequest;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.FixResult;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AppService {
    AnalysisResult retrieve(Library libraryInfo);

    ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception;

    AnalysisResult analyze(MultipartFile file, Boolean includeSafe) throws Exception;

    AnalysisResult analyze(ScanningResult scanningResult,  Boolean includeSafe) throws Exception;

    Severity evaluate(String vector);

    FixResult autoFix(AnalysisResult analysisResult);

    void export(ReportRequest reportRequest, String projectName, String author, String type, String format, HttpServletResponse response) throws Exception;

    AnalysisUIResult reformat(AnalysisResult analysisResult);

    SummaryFix applyFix(FixResult fixResult, MultipartFile file) throws Exception;

}

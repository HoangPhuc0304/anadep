package com.hps.anadep.evaluator.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.response.FixResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

public interface EvaluateService {
    double getScore(String vector);
    String getSeverityRating(double score);

    Severity evaluate(String vector);

    FixResult autoFix(AnalysisResult analysisResult);

    SummaryFix applyFix(FixResult fixResult, MultipartFile file) throws Exception;
}

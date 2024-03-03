package com.hps.osvscanning.evaluator.service;

import com.hps.osvscanning.model.osv.Severity;
import com.hps.osvscanning.model.response.AnalysisResult;
import com.hps.osvscanning.model.ui.AnalysisUIResult;
import com.hps.osvscanning.model.response.FixResult;

public interface EvaluateService {
    double getScore(String vector);
    String getSeverityRating(double score);

    Severity evaluate(String vector);

    FixResult autoFix(AnalysisResult analysisResult);
    FixResult autoFix(AnalysisUIResult analysisUIResult);
}

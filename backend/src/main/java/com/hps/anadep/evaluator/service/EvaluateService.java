package com.hps.anadep.evaluator.service;

import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.response.FixResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.security.AppUser;

public interface EvaluateService {
    double getScore(String vector);
    String getSeverityRating(double score);

    Severity evaluate(String vector);

    FixResult autoFix(AnalysisUIResult analysisUIResult);

    SummaryFix applyFix(AnalysisUIResult analysisUIResult, ScanningResult scanningResult);

    void advisory(Repo repo, AnalysisUIResult analysisUIResult, AppUser appUser);

    void advisoryV2(Repo repo, AnalysisUIResult analysisUIResult, AppUser appUser);
}

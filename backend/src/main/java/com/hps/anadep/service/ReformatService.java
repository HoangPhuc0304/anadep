package com.hps.anadep.service;

import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.ui.AnalysisUIResult;

public interface ReformatService {
    AnalysisUIResult format(AnalysisResult analysisResult);
}

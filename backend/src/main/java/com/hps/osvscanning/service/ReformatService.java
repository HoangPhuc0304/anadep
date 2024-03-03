package com.hps.osvscanning.service;

import com.hps.osvscanning.model.response.AnalysisResult;
import com.hps.osvscanning.model.ui.AnalysisUIResult;

public interface ReformatService {
    AnalysisUIResult format(AnalysisResult analysisResult);
}

package com.hps.osvscanning.analyzer.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.response.AnalysisResult;

import java.util.Set;

public interface AnalyzerService {
    AnalysisResult analyze(Library library);
    AnalysisResult analyze(Set<Library> libraries, Boolean includeSafe) throws Exception;
}

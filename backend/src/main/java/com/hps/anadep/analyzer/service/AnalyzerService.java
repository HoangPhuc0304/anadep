package com.hps.anadep.analyzer.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.response.AnalysisResult;

import java.util.Set;

public interface AnalyzerService {
    AnalysisResult analyze(Library library);
    AnalysisResult analyze(Set<Library> libraries, Boolean includeSafe) throws Exception;
}

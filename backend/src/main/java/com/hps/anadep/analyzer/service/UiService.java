package com.hps.anadep.analyzer.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import org.springframework.web.multipart.MultipartFile;

public interface UiService {
    AnalysisUIResult retrieve(Library library);

    ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception;

    AnalysisUIResult analyze(MultipartFile file, boolean includeSafe) throws Exception;

    byte[] repoDownload(String url);

    Vulnerability getVulnById(String id);
}

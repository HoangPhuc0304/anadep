package com.hps.osvscanning.analyzer.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.Vulnerability;
import com.hps.osvscanning.model.response.ScanningResult;
import com.hps.osvscanning.model.ui.AnalysisUIResult;
import org.springframework.web.multipart.MultipartFile;

public interface UiService {
    AnalysisUIResult retrieve(Library library);

    ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception;

    AnalysisUIResult analyze(MultipartFile file, boolean includeSafe) throws Exception;

    byte[] repoDownload(String url);

    Vulnerability getVulnById(String id);
}

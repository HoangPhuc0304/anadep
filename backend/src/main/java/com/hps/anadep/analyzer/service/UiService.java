package com.hps.anadep.analyzer.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.VulnerabilitySummary;
import com.hps.anadep.security.AppUser;
import org.springframework.web.multipart.MultipartFile;

public interface UiService {
    AnalysisUIResult retrieve(Library library);
    AnalysisUIResult retrieveV2(Library library);
    ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception;
    ScanningResult scan(String repoId, MultipartFile file, boolean includeTransitive, AppUser appUser) throws Exception;
    AnalysisUIResult analyze(MultipartFile file, boolean includeSafe) throws Exception;
    AnalysisUIResult analyze(String repoId, MultipartFile file, boolean includeSafe, AppUser appUser) throws Exception;
    AnalysisUIResult analyzeV2(MultipartFile file, boolean includeSafe) throws Exception;
    AnalysisUIResult analyzeV2(String repoId, MultipartFile file, boolean includeSafe, AppUser appUser) throws Exception;
    byte[] repoDownload(String url, String accessToken);
    Vulnerability getVulnById(String id);
    Vulnerability getVulnByIdV2(String id);
    VulnerabilitySummary summary(AnalysisUIResult analysisUIResult);
    void update(AuthTokenDto authTokenDto);
}

package com.hps.anadep.analyzer.service.impl;

import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.analyzer.client.OsvClient;
import com.hps.anadep.analyzer.service.UiService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.LibraryScan;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.VulnerabilityResponse;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class UiServiceImpl implements UiService {
    @Autowired
    private AppService appService;
    @Autowired
    private GithubClient githubClient;
    @Autowired
    private OsvClient osvClient;

    @Override
    public AnalysisUIResult retrieve(Library library) {
        AnalysisResult analysisResult = appService.retrieve(library);
        List<LibraryScanUI> libs = new ArrayList<>();
        for (LibraryScan libScan : analysisResult.getLibs()) {
            Library info = new Library(libScan.getInfo().getName(), libScan.getInfo().getVersion(), libScan.getInfo().getEcosystem());
            for (VulnerabilityResponse vulRes : libScan.getVulns()) {
                libs.add(new LibraryScanUI(info, vulRes));
            }
        }
        return new AnalysisUIResult(
                libs,
                analysisResult.getEcosystem(),
                analysisResult.getIssuesCount(),
                analysisResult.getLibraryCount(),
                analysisResult.isIncludeSafe(),
                analysisResult.getResponseTime()
        );
    }

    @Override
    public ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception {
        return appService.scan(file, includeTransitive);
    }

    @Override
    public AnalysisUIResult analyze(MultipartFile file, boolean includeSafe) throws Exception {
        AnalysisResult analysisResult = appService.analyze(file, includeSafe);
        return appService.reformat(analysisResult);
    }

    @Override
    public byte[] repoDownload(String url) {
        String repo = getRepoFromGithubUrl(url);
        return githubClient.download(repo);
    }

    @Override
    public Vulnerability getVulnById(String id) {
        return osvClient.getVulnerability(id);
    }

    private String getRepoFromGithubUrl(String url) {
        String repo = url;
        if (url.endsWith(".git")) {
            repo = url.substring(0, url.length() - 4);
        }
        int index = repo.lastIndexOf("/");
        return repo.substring(repo.lastIndexOf("/", index - 1) + 1);
    }
}

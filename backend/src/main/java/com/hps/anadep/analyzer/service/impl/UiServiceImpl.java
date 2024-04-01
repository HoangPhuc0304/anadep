package com.hps.anadep.analyzer.service.impl;

import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.analyzer.client.OsvClient;
import com.hps.anadep.analyzer.service.UiService;
import com.hps.anadep.evaluator.enums.Severity;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.LibraryScan;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.VulnerabilityResponse;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.LibraryScanUI;
import com.hps.anadep.model.ui.VulnerabilitySummary;
import com.hps.anadep.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
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
    public byte[] repoDownload(String url, String accessToken) {
        String repo = getRepoFromGithubUrl(url);
        return githubClient.download(repo, accessToken);
    }

    @Override
    public Vulnerability getVulnById(String id) {
        return osvClient.getVulnerability(id);
    }

    @Override
    public VulnerabilitySummary summary(AnalysisUIResult analysisUIResult) {
        VulnerabilitySummary vulnerabilitySummary = new VulnerabilitySummary();
        if (CollectionUtils.isEmpty(analysisUIResult.getLibs())) {
            return vulnerabilitySummary;
        }
        analysisUIResult.getLibs().forEach(result -> {
            List<com.hps.anadep.model.osv.Severity> severities = result.getVuln().getSeverity();
            if (!CollectionUtils.isEmpty(severities)) {
                Severity severity = Severity.getSeverityFromName(result.getVuln().getSeverity().get(0).getRanking());
                switch (severity) {
                    case LOW -> vulnerabilitySummary.setLow(vulnerabilitySummary.getLow() + 1);
                    case MEDIUM -> vulnerabilitySummary.setMedium(vulnerabilitySummary.getMedium() + 1);
                    case HIGH -> vulnerabilitySummary.setHigh(vulnerabilitySummary.getHigh() + 1);
                    case CRITICAL -> vulnerabilitySummary.setCritical(vulnerabilitySummary.getCritical() + 1);
                    default -> vulnerabilitySummary.setNone(vulnerabilitySummary.getNone() + 1);
                }
            }
        });
        return vulnerabilitySummary;
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

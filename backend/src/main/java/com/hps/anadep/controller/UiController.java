package com.hps.anadep.controller;

import com.hps.anadep.analyzer.service.UiService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.model.ui.VulnerabilitySummary;
import com.hps.anadep.security.AppUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UiController {

    @Autowired
    private UiService uiService;

    @PostMapping("/ui/retrieve")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult scan(@RequestBody Library library) {
        return uiService.retrieve(library);
    }

    @PostMapping("/ui/retrieve/v2")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult scanV2(@RequestBody Library library) {
        return uiService.retrieveV2(library);
    }

    @PostMapping("/ui/scan")
    @ResponseStatus(HttpStatus.OK)
    public ScanningResult scan(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "includeTransitive", defaultValue = "true") boolean includeTransitive)
            throws Exception {
        return uiService.scan(file, includeTransitive);
    }

    @PostMapping("/ui/scan/repo/{repoId}")
    @ResponseStatus(HttpStatus.OK)
    public ScanningResult scan(@PathVariable("repoId") String repoId,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam(value = "includeTransitive", defaultValue = "true") boolean includeTransitive,
                               @AuthenticationPrincipal AppUser appUser)
            throws Exception {
        return uiService.scan(repoId, file, includeTransitive, appUser);
    }

    @PostMapping("/ui/analyze")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult analyze(@RequestParam("file") MultipartFile file)
            throws Exception {
        return uiService.analyze(file, false);
    }

    @PostMapping("/ui/analyze/repo/{repoId}")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult analyze(@PathVariable("repoId") String repoId,
                                    @RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal AppUser appUser)
            throws Exception {
        return uiService.analyze(repoId, file, false, appUser);
    }

    @PostMapping("/ui/analyze/v2")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult analyzeV2(@RequestParam("file") MultipartFile file)
            throws Exception {
        return uiService.analyzeV2(file, false);
    }

    @PostMapping("/ui/analyze/v2/repo/{repoId}")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult analyzeV2(@PathVariable("repoId") String repoId,
                                    @RequestParam("file") MultipartFile file,
                                    @AuthenticationPrincipal AppUser appUser)
            throws Exception {
        return uiService.analyzeV2(repoId, file, false, appUser);
    }

    @GetMapping("/ui/repo/download")
    @ResponseStatus(HttpStatus.OK)
    public byte[] repoDownload(@RequestParam("url") String url,
                               @RequestParam(value = "accessToken", required = false) String accessToken)
            throws Exception {
        return uiService.repoDownload(url, accessToken);
    }

    @GetMapping("/ui/vulns/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Vulnerability getVuln(@PathVariable("id") String id){
        return uiService.getVulnById(id);
    }

    @GetMapping("/ui/vulns/{id}/v2")
    @ResponseStatus(HttpStatus.OK)
    public Vulnerability getVulnV2(@PathVariable("id") String id){
        return uiService.getVulnByIdV2(id);
    }

    @PostMapping("/ui/summary")
    @ResponseStatus(HttpStatus.OK)
    public VulnerabilitySummary analyze(@RequestBody AnalysisUIResult analysisUIResult) {
        return uiService.summary(analysisUIResult);
    }

    @PostMapping("/ui/user/token")
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody AuthTokenDto authTokenDto) {
        uiService.update(authTokenDto);
    }
}

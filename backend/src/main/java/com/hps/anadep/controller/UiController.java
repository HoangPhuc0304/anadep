package com.hps.anadep.controller;

import com.hps.anadep.analyzer.service.UiService;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.Vulnerability;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/ui/scan")
    @ResponseStatus(HttpStatus.OK)
    public ScanningResult scan(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "includeTransitive", defaultValue = "true") boolean includeTransitive)
            throws Exception {
        return uiService.scan(file, includeTransitive);
    }

    @PostMapping("/ui/analyze")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult analyze(@RequestParam("file") MultipartFile file)
            throws Exception {
        return uiService.analyze(file, false);
    }

    @GetMapping("/ui/repo/download")
    @ResponseStatus(HttpStatus.OK)
    public byte[] repoDownload(@RequestParam("url") String url)
            throws Exception {
        return uiService.repoDownload(url);
    }

    @GetMapping("/ui/vulns/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Vulnerability getVuln(@PathVariable("id") String id){
        return uiService.getVulnById(id);
    }
}

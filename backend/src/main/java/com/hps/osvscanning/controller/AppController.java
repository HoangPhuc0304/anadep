package com.hps.osvscanning.controller;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.Severity;
import com.hps.osvscanning.model.response.FixResult;
import com.hps.osvscanning.model.response.AnalysisResult;
import com.hps.osvscanning.model.response.ScanningResult;
import com.hps.osvscanning.model.ui.AnalysisUIResult;
import com.hps.osvscanning.service.AppService;
import com.hps.osvscanning.service.ReformatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping("/api/retrieve")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult scan(@RequestBody Library library) {
        return appService.retrieve(library);
    }

    @PostMapping("/api/scan")
    @ResponseStatus(HttpStatus.OK)
    public ScanningResult scan(@RequestParam("file") MultipartFile file,
                               @RequestParam(value = "includeTransitive", defaultValue = "true") boolean includeTransitive)
            throws Exception {
        return appService.scan(file, includeTransitive);
    }

    @PostMapping("/api/analyze")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult analyze(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "includeSafe", defaultValue = "false") boolean includeSafe)
            throws Exception {
        return appService.analyze(file, includeSafe);
    }

    @GetMapping("/api/evaluate")
    @ResponseStatus(HttpStatus.OK)
    public Severity evaluate(@RequestParam("vector") String vector) {
        return appService.evaluate(vector);
    }

    @PostMapping("/api/auto-fix")
    @ResponseStatus(HttpStatus.OK)
    public FixResult autoFix(@RequestBody AnalysisResult analysisResult) throws Exception {
        return appService.autoFix(analysisResult);
    }
}

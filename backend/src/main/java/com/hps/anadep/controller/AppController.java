package com.hps.anadep.controller;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.github.WebhookPayload;
import com.hps.anadep.model.report.ReportRequest;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.FixResult;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.security.AppUser;
import com.hps.anadep.service.AppService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/api/retrieve/v2")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult scanV2(@RequestBody Library library) {
        return appService.retrieveV2(library);
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

    @PostMapping("/api/analyze/v2")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult analyzeV2(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "includeSafe", defaultValue = "false") boolean includeSafe)
            throws Exception {
        return appService.analyzeV2(file, includeSafe);
    }

    @PostMapping("/api/analyze-fast")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult analyzeFast(@RequestBody ScanningResult scanningResult,
                                  @RequestParam(value = "includeSafe", defaultValue = "false") boolean includeSafe)
            throws Exception {
        return appService.analyzeFast(scanningResult, includeSafe);
    }

    @PostMapping("/api/analyze-fast/v2")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult analyzeFastV2(@RequestBody ScanningResult scanningResult,
                                        @RequestParam(value = "includeSafe", defaultValue = "false") boolean includeSafe)
            throws Exception {
        return appService.analyzeFastV2(scanningResult, includeSafe);
    }

    @GetMapping("/api/evaluate")
    @ResponseStatus(HttpStatus.OK)
    public Severity evaluate(@RequestParam("vector") String vector) {
        return appService.evaluate(vector);
    }

    @PostMapping("/api/report/reformat")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisUIResult reformat(@RequestBody AnalysisResult analysisResult) {
        return appService.reformat(analysisResult);
    }

    @PostMapping("/api/report")
    @ResponseStatus(HttpStatus.OK)
    public void export(
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam("type") String type,
            @RequestParam("format") String format,
            @RequestBody ReportRequest reportRequest,
            HttpServletResponse response) throws Exception {
        appService.export(reportRequest, projectName, author, type, format, response);
    }

    @PostMapping("/api/auto-fix")
    @ResponseStatus(HttpStatus.OK)
    public FixResult autoFix(@RequestBody AnalysisUIResult analysisUIResult) {
        return appService.autoFix(analysisUIResult);
    }

    @PostMapping("/api/fix/repo/{repoId}/history/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public SummaryFix applyFix(@PathVariable("repoId") String repoId,
                               @PathVariable("historyId") String historyId,
                               @AuthenticationPrincipal AppUser appUser) throws Exception {
        return appService.applyFix(repoId, historyId, appUser);
    }

    @PostMapping("/api/security-advisories/repo/{repoId}/history/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public void advisory(@PathVariable("repoId") String repoId,
                         @PathVariable("historyId") String historyId,
                         @AuthenticationPrincipal AppUser appUser) {
        appService.advisory(repoId, historyId, appUser);
    }

    @PostMapping("/api/security-advisories/v2/repo/{repoId}/history/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public void advisoryV2(@PathVariable("repoId") String repoId,
                           @PathVariable("historyId") String historyId,
                           @AuthenticationPrincipal AppUser appUser) {
        appService.advisoryV2(repoId, historyId, appUser);
    }
}

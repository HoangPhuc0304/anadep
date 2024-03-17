package com.hps.anadep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.report.ReportRequest;
import com.hps.anadep.model.osv.Severity;
import com.hps.anadep.model.response.FixResult;
import com.hps.anadep.model.response.AnalysisResult;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.service.AppService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping("/api/analyze/v2")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisResult analyze(@RequestBody ScanningResult scanningResult,
                                  @RequestParam(value = "includeSafe", defaultValue = "false") boolean includeSafe)
            throws Exception {
        return appService.analyze(scanningResult, includeSafe);
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
    public FixResult autoFix(@RequestBody AnalysisResult analysisResult) {
        return appService.autoFix(analysisResult);
    }

    @PostMapping("/api/apply-fix")
    @ResponseStatus(HttpStatus.OK)
    public SummaryFix applyFix(@RequestParam("fixResult") String fixResultStr, @RequestParam("file") MultipartFile file) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        FixResult fixResult = objectMapper.readValue(fixResultStr, FixResult.class);
        return appService.applyFix(fixResult, file);
    }
}

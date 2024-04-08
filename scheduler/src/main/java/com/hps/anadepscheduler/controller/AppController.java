package com.hps.anadepscheduler.controller;

import com.hps.anadepscheduler.model.osv.*;
import com.hps.anadepscheduler.service.AppService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppController {
    @Autowired
    private AppService appService;

    @PostMapping("/v1/query")
    @ResponseStatus(HttpStatus.OK)
    public VulnerabilityOSVResponse queryDependency(@Valid @RequestBody LibraryOSVRequest libraryOSVRequest) {
        return appService.query(libraryOSVRequest);
    }

    @PostMapping("/v1/querybatch")
    @ResponseStatus(HttpStatus.OK)
    public VulnerabilityOSVBatchResponse queryDependency(@Valid @RequestBody LibraryOSVBatchRequest libraryOSVBatchRequest) {
        return appService.query(libraryOSVBatchRequest);
    }

    @GetMapping("/v1/vulns/{databasedId}")
    @ResponseStatus(HttpStatus.OK)
    public Vulnerability queryDependency(@PathVariable("databasedId") String databasedId) {
        return appService.query(databasedId);
    }
}

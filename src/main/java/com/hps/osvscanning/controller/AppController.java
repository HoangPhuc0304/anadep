package com.hps.osvscanning.controller;

import com.hps.osvscanning.model.VulnerabilityBulk;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.service.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AppController {
    @Autowired
    private ScanService scanService;

    @PostMapping("/api/retrieve")
    @ResponseStatus(HttpStatus.OK)
    public VulnerabilityBulk scan(@RequestBody Library libraryInfo) {
        return scanService.retrieve(libraryInfo);
    }

    @PostMapping("/api/scan")
    @ResponseStatus(HttpStatus.OK)
    public VulnerabilityBulk scan(@RequestParam("file") MultipartFile file) throws Exception {
        return scanService.scan(file);
    }
}

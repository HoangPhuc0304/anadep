package com.hps.anadep.scanner.service;

import com.hps.anadep.model.response.ScanningResult;
import org.springframework.web.multipart.MultipartFile;

public interface ScannerService {
    ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception;
}

package com.hps.osvscanning.scanner.service;

import com.hps.osvscanning.model.Library;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ScannerService {
    Set<Library> scan(MultipartFile file, boolean includeTransitive) throws Exception;
}

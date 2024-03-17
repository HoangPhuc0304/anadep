package com.hps.anadep.scanner.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.Library;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public interface ScannerService {
    Set<Library> scan(MultipartFile file, boolean includeTransitive) throws Exception;
    Map<Library, String> scanToMap(MultipartFile file) throws Exception;

    boolean isUseLibrary(Map.Entry<Library, String> entry, Library lib) throws JsonProcessingException;
}

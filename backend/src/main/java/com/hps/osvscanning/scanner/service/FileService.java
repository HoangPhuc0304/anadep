package com.hps.osvscanning.scanner.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void save(MultipartFile file, String filename, String namespace);
    void clean(String namespace);
}

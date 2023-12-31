package com.hps.osvscanning.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void save(MultipartFile file);
    void clean();
}

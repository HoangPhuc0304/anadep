package com.hps.anadep.scanner.service;

import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;

public interface FileService {
    void save(MultipartFile file, String filename, String namespace);
    void clean(String namespace);

    void writeXml(Document doc, String mavenScannerDir, String fileName) throws Exception;
}

package com.hps.osvscanning.service.impl;

import com.hps.osvscanning.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
public class FileServiceImpl implements FileService {
    private static final String STORAGE = "storage";
    private static final String POM_XML = "pom.xml";
    private static final String TEXT = "dependency.txt";
    private final Path root = Paths.get(STORAGE);

    public FileServiceImpl() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), root.resolve(POM_XML), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clean() {
        try {
            File file = new File(STORAGE);
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

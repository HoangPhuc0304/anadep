package com.hps.osvscanning.scanner.service.impl;

import com.hps.osvscanning.model.enums.Compress;
import com.hps.osvscanning.scanner.service.FileService;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.hps.osvscanning.scanner.util.FileStorage.*;

@Service
public class FileServiceImpl implements FileService {
    private final Path root = Paths.get(STORAGE_DIR);

    public FileServiceImpl() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file, String filename, String namespace) {
        try {
            String destinationDir = String.join("/", STORAGE_DIR, namespace);
            Files.createDirectories(Paths.get(destinationDir));
            String folderName = filename.substring(0, filename.lastIndexOf("."));
            String extension = filename.substring(filename.lastIndexOf('.') + 1);
            if (extension.equals(Compress.ZIP.getName())) {
                String filePath = String.join("/",destinationDir, filename);
                downloadZipFile(file, filePath);
                ZipFile zipFile = new ZipFile(filePath);
                if (zipFile.isEncrypted()) {
                    throw new RuntimeException("Cannot extract zip file");
                }
                zipFile.extractAll(String.join("/",destinationDir, folderName));
            } else {
                Files.copy(file.getInputStream(), root.resolve(String.join("/", namespace, filename)), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("A file of that name already exists.");
            }

            throw new RuntimeException(e.getMessage());
        }
    }

    private void downloadZipFile(MultipartFile file, String filePath) throws Exception {
        try (InputStream in = file.getInputStream();
             FileOutputStream out = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public void clean(String namespace) {
        try {
            File file = new File(String.join("/", STORAGE_DIR, namespace));
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

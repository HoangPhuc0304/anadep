package com.hps.anadepscheduler.util;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileUtil {
    public void downloadZipFile(String fileUrl, String filePath) throws Exception {
        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    public void delete(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clean(String namespace) {
        try {
            File file = new File(namespace);
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

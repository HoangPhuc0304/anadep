package com.hps.anadep.model.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@NoArgsConstructor
@AllArgsConstructor
public class CustomMultipartFile implements MultipartFile {
    private String nameField;
    private String originName;
    private String contentType;
    private byte[] input;

    @Override
    public String getName() {
        return nameField;
    }

    @Override
    public String getOriginalFilename() {
        return originName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return input == null || input.length == 0;
    }

    @Override
    public long getSize() {
        return input.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return input;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(input);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try(FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(input);
        }
    }
}

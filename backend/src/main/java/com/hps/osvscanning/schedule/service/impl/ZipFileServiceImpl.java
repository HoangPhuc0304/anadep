//package com.hps.osvscanning.schedule.service.impl;
//
//import com.hps.osvscanning.schedule.service.ZipFileService;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//public class ZipFileServiceImpl implements ZipFileService {
//    @Override
//    public void saveToFile(byte[] content, String filePath) {
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            fos.write(content);
//        } catch (IOException e) {
//            throw new RuntimeException("Error saving file", e);
//        }
//    }
//
//    @Override
//    public void unzip(String zipFilePath, String destinationFolder) throws IOException {
//        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
//            ZipEntry entry;
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                String entryName = entry.getName();
//                String filePath = destinationFolder + File.separator + entryName;
//
//                if (entry.isDirectory()) {
//                    Files.createDirectories(Paths.get(filePath));
//                } else {
//                    try (OutputStream outputStream = new FileOutputStream(filePath)) {
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
//                            outputStream.write(buffer, 0, bytesRead);
//                        }
//                    }
//                }
//                zipInputStream.closeEntry();
//            }
//        }
//    }
//
//    @Override
//    public void clean() {
//
//    }
//}

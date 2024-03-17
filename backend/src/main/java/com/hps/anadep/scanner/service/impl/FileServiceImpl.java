package com.hps.anadep.scanner.service.impl;

import com.hps.anadep.model.enums.Compress;
import com.hps.anadep.scanner.service.FileService;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.*;

import static com.hps.anadep.scanner.util.FileStorage.*;

@Service
public class FileServiceImpl implements FileService {
    private final Path root = Paths.get(SCANNER_DIR);

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
            String destinationDir = String.join("/", SCANNER_DIR, namespace);
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
            File file = new File(namespace);
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void writeXml(Document doc, String mavenScannerDir, String fileName) throws Exception {
        Files.createDirectories(Paths.get(mavenScannerDir));
        FileOutputStream output = new FileOutputStream(String.join("/", mavenScannerDir, fileName));
        writeXml(doc, output);
        output.close();
    }

    private static void writeXml(Document doc, OutputStream output) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);
    }
}

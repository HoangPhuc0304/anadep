package com.hps.anadep.scanner.util;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.enums.MavenType;
import com.hps.anadep.scanner.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.hps.anadep.scanner.util.FileStorage.*;

@Component
public class MavenTool implements PackageManagementTool {
    @Autowired
    private FileService fileService;

    @Value("classpath:scanner-template.xml")
    Resource resourceFile;

    @Override
    public Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String storagePomXml = String.join("/", SCANNER_DIR, namespace, Ecosystem.MAVEN.getPackageManagementFile());
        String storageText = String.join("/", SCANNER_DIR, namespace, STORAGE_TEXT_FILE_NAME);
        Set<Library> libraries = new HashSet<>();

        processing(storagePomXml, STORAGE_TEXT_FILE_NAME, includeTransitive);

        try (BufferedReader reader = new BufferedReader(new FileReader(storageText))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    String groupId = parts[0].strip();
                    String artifactId = parts[1].strip();
                    String version = parts[3].strip();
                    String name = String.format("%s:%s", groupId, artifactId);
                    libraries.add(new Library(name, version, Ecosystem.MAVEN.getOsvName()));
                }
            }
        }

        return libraries;
    }

    @Override
    public Map<Library, String> getDependencyMap(String namespace) throws Exception {
        Set<Library> visibleDependencies = getDependencies(false, namespace);
        ExecutorService executorService = Executors.newFixedThreadPool(visibleDependencies.size());
        List<CompletableFuture<AbstractMap.SimpleEntry<Library, String>>> completableFutures = visibleDependencies.stream()
                .map(library -> CompletableFuture.supplyAsync(() -> {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    try (InputStream is = resourceFile.getInputStream()) {
                        DocumentBuilder db = dbf.newDocumentBuilder();
                        Document doc = db.parse(is);
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        Node groupId = (Node) xPath.compile("//dependency/groupId").evaluate(doc, XPathConstants.NODE);
                        groupId.setTextContent(library.getName().split(":")[0]);
                        Node artifactId = (Node) xPath.compile("//dependency/artifactId").evaluate(doc, XPathConstants.NODE);
                        artifactId.setTextContent(library.getName().split(":")[1]);
                        Node version = (Node) xPath.compile("//dependency/version").evaluate(doc, XPathConstants.NODE);
                        version.setTextContent(library.getVersion());
                        String fileName = String.format("scanner-%s.xml", UUID.randomUUID());
                        fileService.writeXml(doc, String.join("/", SCANNER_DIR, namespace), fileName);

                        String storagePomXml = String.join("/", SCANNER_DIR, namespace, fileName);
                        String generatedFile = fileName.replace(".xml", ".txt");
                        String storageText = String.join("/", SCANNER_DIR, namespace, generatedFile);

                        processing(storagePomXml, generatedFile, true);

                        StringBuilder sb = new StringBuilder();
                        try (BufferedReader reader = new BufferedReader(new FileReader(storageText))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                sb.append(line);
                            }
                        }
                        return new AbstractMap.SimpleEntry<>(library, sb.toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executorService))
                .toList();

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

        Map<Library, String> result =
                completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        executorService.shutdown();
        return result;
    }

    @Override
    public boolean isUseLibrary(Map.Entry<Library, String> libraryMap, Library lib) {
        return libraryMap.getValue().contains(String.format("%s:%s:%s", lib.getName(), MavenType.JAVA_SOURCE.getName(), lib.getVersion()))
                || libraryMap.getValue().contains(String.format("%s:%s:%s", lib.getName(), MavenType.JAR.getName(), lib.getVersion()))
                || libraryMap.getValue().contains(String.format("%s:%s:%s", lib.getName(), MavenType.WAR.getName(), lib.getVersion()));
    }

    private void processing(String storagePomXml, String generatedFile, boolean includeTransitive) throws Exception {
        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    String.format("./mvnw dependency:list --file %s -DoutputFile=%s -DexcludeTransitive=%b",
                            storagePomXml, generatedFile, !includeTransitive));
        } catch (Exception exc) {
            process = Runtime.getRuntime().exec(
                    String.format("./mvnw.cmd dependency:list --file %s -DoutputFile=%s -DexcludeTransitive=%b",
                            storagePomXml, generatedFile, !includeTransitive));
        }
//        process.waitFor();

//        Process process;
//        try {
//            process = Runtime.getRuntime().exec(
//                    String.format("bash script/maven-get-dependencies.sh %s %s %s",
//                            storagePomXml, storageText, !includeTransitive));
//        } catch (Exception exc) {
//            throw new RuntimeException("There are something wrong with scanning");
//        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);
        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
    }

}

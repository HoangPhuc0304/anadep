package com.hps.anadep.scanner.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.npm.Dependencies;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.hps.anadep.scanner.util.FileStorage.*;

@Component
@Slf4j
public class NpmTool implements PackageManagementTool {

    @Override
    public Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String storagePackageDir = String.join("/", SCANNER_DIR, namespace);
        String storageJson = String.join("/", SCANNER_DIR, namespace, STORAGE_JSON_FILE_NAME);
        Set<Library> libraries = new HashSet<>();
        File file = new File(storageJson);

        processing(storagePackageDir, includeTransitive);

        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Dependencies dependencies = objectMapper.readValue(content, Dependencies.class);

        populateToLibraries(dependencies.getDependencies(), libraries);
        return libraries;

//        if (isWindows) {
//            builder.command(
//                    "script\\npm-get-dependencies.bat",
//                    storagePackageDir.replace("/", "\\"),
//                    includeTransitive ? "--all" : "",
//                    STORAGE_JSON_FILE_NAME
//            );
//        } else {
//            builder.command("sh", "-c", System.getProperty("user.dir") + String.format("/script/npm-get-dependencies.sh %s %s %s",
//                    storagePackageDir, includeTransitive ? "--all" : "", STORAGE_JSON_FILE_NAME));
//        }
//        builder.directory(new File(System.getProperty("user.dir")));
//
//        ExecutorService pool = Executors.newSingleThreadExecutor();
//
//        try {
//            Process process = builder.start();
//            ProccessReader task = new ProccessReader(process.getInputStream());
//            Future<List<String>> future = pool.submit(task);
//
//            List<String> results = future.get();
//            for(String result : results) {
//                System.out.println(result);
//            }
//            int exitCode = process.waitFor();
//            System.out.println("Exit code: "+ exitCode);
//        } catch (IOException | ExecutionException | InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            pool.shutdown();
//        }
    }

    @Override
    public Map<Library, String> getDependencyMap(String namespace) throws Exception {
        String storagePackageDir = String.join("/", SCANNER_DIR, namespace);
        String storageJson = String.join("/", SCANNER_DIR, namespace, STORAGE_JSON_FILE_NAME);
        File file = new File(storageJson);

        processing(storagePackageDir, true);

        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Dependencies dependencies = objectMapper.readValue(content, Dependencies.class);
        Map<Library, String> map = new HashMap<>();
        for (Map.Entry<String, Dependencies> entry : dependencies.getDependencies().entrySet()) {
            map.put(new Library(entry.getKey(), entry.getValue().getVersion(), Ecosystem.NPM.getOsvName()), objectMapper.writeValueAsString(entry.getValue()));
        }
        return map;
    }

    @Override
    public boolean isUseLibrary(Map.Entry<Library, String> libraryMap, Library lib) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Dependencies dependencies = objectMapper.readValue(libraryMap.getValue(), Dependencies.class);
        SingletonMap<String, Dependencies> singletonMap = new SingletonMap<>(libraryMap.getKey().getName(), dependencies);
        return isUseLibrary(singletonMap, lib);
    }

    private void processing(String storagePackageDir, boolean includeTransitive) throws Exception {
        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    String.format("bash script/npm-get-dependencies.sh %s %s %s",
                            storagePackageDir, includeTransitive ? "all" : "none", STORAGE_JSON_FILE_NAME));
        } catch (Exception exc) {
            log.error(exc.getMessage());
            throw new RuntimeException("There are something wrong with scanning");
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);
        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
    }

    public boolean isUseLibrary(SingletonMap<String, Dependencies> map, Library lib) {
        if (map == null || CollectionUtils.isEmpty(map) || map.getValue() == null) {
            return false;
        }  else if (map.getKey().equals(lib.getName())) {
            if (!StringUtils.hasText(map.getValue().getVersion())) {
                return !StringUtils.hasText(lib.getVersion());
            }
            return map.getValue().getVersion().equals(lib.getVersion());
        } else if (CollectionUtils.isEmpty(map.getValue().getDependencies())) {
            return false;
        }

        for (Map.Entry<String, Dependencies> entry : map.getValue().getDependencies().entrySet()) {
            SingletonMap<String, Dependencies> singletonMap = new SingletonMap<>(entry.getKey(), entry.getValue());
            if (isUseLibrary(singletonMap, lib)) {
                return true;
            }
        }
        return false;
    }

    private void populateToLibraries(Map<String, Dependencies> dependencies, Set<Library> libraries) {
        if (dependencies == null) {
            return;
        }

        for (String key : dependencies.keySet()) {
            libraries.add(new Library(key, dependencies.get(key).getVersion(), Ecosystem.NPM.getOsvName()));
            populateToLibraries(dependencies.get(key).getDependencies(), libraries);
        }
    }
}

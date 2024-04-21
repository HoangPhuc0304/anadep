package com.hps.anadep.scanner.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.SummaryLibraryFix;
import com.hps.anadep.model.depgraph.Dependency;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.npm.Dependencies;
import com.hps.anadep.model.npm.PackageJson;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hps.anadep.scanner.util.FileStorage.*;

@Component
@Slf4j
public class NpmTool implements PackageManagementTool {

    private static final String NPM_FORMAT_ID = "%s:%s";
    private static final String BEFORE_FILE_FORMAT = "storage/fix/before/%s";
    private static final String AFTER_FILE_FORMAT = "storage/fix/after/%s";
    private static final String DEPENDENCY_NAME_FORMAT = "%s:%s";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ScanningResult getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String storagePackageDir = String.join("/", SCANNER_DIR, namespace);
        String storageJson = String.join("/", SCANNER_DIR, namespace, STORAGE_JSON_FILE_NAME);
        Set<Library> libraries = new HashSet<>();
        Set<Dependency> dependencies = new HashSet<>();
        File file = new File(storageJson);

        processing(storagePackageDir, STORAGE_JSON_FILE_NAME);

        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Dependencies deps = objectMapper.readValue(content, Dependencies.class);

        libraries.add(new Library(deps.getName(),deps.getVersion(), Ecosystem.NPM.getOsvName()));
        String id = NPM_FORMAT_ID.formatted(deps.getName(), deps.getVersion());

        populateData(deps.getDependencies(), libraries, dependencies, id);

        if (!includeTransitive) {
            Map<String, Library> libMap = libraries.stream().collect(Collectors.toMap(
                    library -> NPM_FORMAT_ID.formatted(library.getName(), library.getVersion()),
                    Function.identity()));
            List<String> ids = dependencies
                    .stream().filter(d -> d.getFrom().equals(id))
                    .map(Dependency::getTo).toList();
            libraries = ids.stream().map(libMap::get).collect(Collectors.toSet());
        }

        return ScanningResult.builder()
                .libraries(libraries)
                .dependencies(dependencies)
                .projectName(id)
                .ecosystem(Ecosystem.MAVEN.getOsvName())
                .libraryCount(libraries.size())
                .includeTransitive(includeTransitive)
                .build();
    }

    @Override
    public void createFixFile(SummaryFix summaryFix, String fileName) throws Exception {
        File file = new File(BEFORE_FILE_FORMAT.formatted(fileName));
        PackageJson packageJson = objectMapper.readValue(file, PackageJson.class);

        Map<String, Object> overrides = packageJson.getOverrides();

        if (overrides == null) {
            overrides = new HashMap<>();
        }

        for (SummaryLibraryFix fix : summaryFix.getLibs()) {
            if (StringUtils.hasText(fix.getFixedVersion())) {
                overrides.put(fix.getName(), fix.getFixedVersion());
            }
        }

        packageJson.setOverrides(overrides);
        byte[] byteContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(packageJson);
        FileUtils.writeByteArrayToFile(new File(AFTER_FILE_FORMAT.formatted(fileName)), byteContent);
    }

    private void processing(String storagePackageDir,String generatedFile) throws Exception {
        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    String.format("bash script/npm-get-dependencies.sh %s %s",
                            storagePackageDir, generatedFile));
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

    //DFS
    private void populateData(Map<String, Dependencies> depMap, Set<Library> libraries, Set<Dependency> dependencies, String id) {
        if (depMap == null) {
            return;
        }

        for (String key : depMap.keySet()) {
            libraries.add(new Library(key, depMap.get(key).getVersion(), Ecosystem.NPM.getOsvName()));
            String depId = NPM_FORMAT_ID.formatted(key, depMap.get(key).getVersion());
            dependencies.add(
                    Dependency.builder()
                            .from(id)
                            .to(depId)
                            .build()
            );
            populateData(depMap.get(key).getDependencies(), libraries, dependencies, depId);
        }
    }
}

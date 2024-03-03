package com.hps.osvscanning.scanner.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Ecosystem;
import com.hps.osvscanning.model.npm.Dependencies;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static com.hps.osvscanning.scanner.util.FileStorage.*;

@Component
@Slf4j
public class NpmTool implements PackageManagementTool {
    @Override
    public Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String storagePackageDir = String.join("/", STORAGE_DIR, namespace);
        String storageJson = String.join("/", STORAGE_DIR, namespace, STORAGE_JSON_FILE_NAME);
        Set<Library> libraries = new HashSet<>();
        File file = new File(storageJson);

        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    String.format("bash script/npm-get-dependencies.sh \"%s\" \"%s\" \"%s\"",
                            storagePackageDir, includeTransitive ? "--all" : "", STORAGE_JSON_FILE_NAME));
        } catch (Exception exc) {
            log.error(exc.getMessage());
            throw new RuntimeException("There are something wrong with scanning");
        }
        process.waitFor();

        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        Dependencies dependencies = objectMapper.readValue(content, Dependencies.class);

        populateToLibraries(dependencies.getDependencies(), libraries);
        return libraries;
    }

    private void populateToLibraries(HashMap<String, Dependencies> dependencies, Set<Library> libraries) {
        if (dependencies == null) {
            return;
        }

        for (String key : dependencies.keySet()) {
            libraries.add(new Library(key, dependencies.get(key).getVersion(), Ecosystem.NPM.getOsvName()));
            populateToLibraries(dependencies.get(key).getDependencies(), libraries);
        }
    }
}

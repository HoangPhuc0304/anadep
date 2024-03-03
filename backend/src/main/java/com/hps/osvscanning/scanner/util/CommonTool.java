package com.hps.osvscanning.scanner.util;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Ecosystem;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hps.osvscanning.scanner.util.FileStorage.STORAGE_DIR;
import static com.hps.osvscanning.scanner.util.FileStorage.STORAGE_TEXT_FILE_NAME;

@Component
public class CommonTool implements PackageManagementTool {
    public Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String destinationDir = String.join("/", STORAGE_DIR, namespace);
        Ecosystem ecosystem = null;
        for (Ecosystem ecos : Ecosystem.values()) {
            if (Files.exists(Paths.get(destinationDir, ecos.getPackageManagementFile()))) {
                ecosystem = ecos;
                break;
            }
        }

        if (ecosystem == null) {
            String folderName;
            try (Stream<Path> stream = Files.list(Paths.get(destinationDir))) {
                folderName = stream.filter(Files::isDirectory)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .findFirst().get();
            }

            namespace = String.join("/", namespace, folderName);

            for (Ecosystem ecos : Ecosystem.values()) {
                if (Files.exists(Paths.get(destinationDir, folderName, ecos.getPackageManagementFile()))) {
                    ecosystem = ecos;
                    break;
                }
            }
        }

        if (Objects.isNull(ecosystem)) {
            return Collections.emptySet();
        }

        PackageManagementTool packageManagementTool = null;
        switch (ecosystem) {
            case MAVEN -> {
                packageManagementTool = new MavenTool();
            }
            case NPM -> {
                packageManagementTool = new NpmTool();
            }
            default -> {
                return Collections.emptySet();
            }
        }
        Set<Library> libraries = packageManagementTool.getDependencies(includeTransitive, namespace);
        return libraries;
    }
}

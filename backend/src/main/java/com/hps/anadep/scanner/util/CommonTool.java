package com.hps.anadep.scanner.util;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.enums.Ecosystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static com.hps.anadep.scanner.util.FileStorage.SCANNER_DIR;

@Component
public class CommonTool implements PackageManagementTool {
    @Autowired
    private ApplicationContext applicationContext;

    public Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String destinationDir = String.join("/", SCANNER_DIR, namespace);
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

        PackageManagementTool packageManagementTool;
        switch (ecosystem) {
            case MAVEN -> {
                packageManagementTool = applicationContext.getBean(MavenTool.class);
            }
            case NPM -> {
                packageManagementTool = applicationContext.getBean(NpmTool.class);
            }
            default -> {
                return Collections.emptySet();
            }
        }
        Set<Library> libraries = packageManagementTool.getDependencies(includeTransitive, namespace);
        return libraries;
    }

    @Override
    public boolean isUseLibrary(Map.Entry<Library, String> libraryMap, Library lib) {
        return false;
    }

    @Override
    public Map<Library, String> getDependencyMap(String namespace) throws Exception {
        String destinationDir = String.join("/", SCANNER_DIR, namespace);
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
            return Collections.emptyMap();
        }

        PackageManagementTool packageManagementTool;
        switch (ecosystem) {
            case MAVEN -> {
                packageManagementTool = applicationContext.getBean(MavenTool.class);
            }
            case NPM -> {
                packageManagementTool = applicationContext.getBean(NpmTool.class);
            }
            default -> {
                return Collections.emptyMap();
            }
        }
        return packageManagementTool.getDependencyMap(namespace);
    }
}

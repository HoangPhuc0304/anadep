package com.hps.anadep.scanner.util;

import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static com.hps.anadep.scanner.util.FileStorage.SCANNER_DIR;

@Component
public class CommonTool implements PackageManagementTool {
    @Autowired
    private ApplicationContext applicationContext;

    public ScanningResult getDependencies(boolean includeTransitive, String namespace) throws Exception {
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
            return new ScanningResult();
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
                return new ScanningResult();
            }
        }
        return packageManagementTool.getDependencies(includeTransitive, namespace);
    }

    @Override
    public void createFixFile(SummaryFix summaryFix, String fileName) throws Exception {
        PackageManagementTool packageManagementTool = null;
        Ecosystem ecosystem = Ecosystem.getEcosystem(summaryFix.getEcosystem());
        switch (ecosystem) {
            case MAVEN -> packageManagementTool = applicationContext.getBean(MavenTool.class);
            case NPM -> packageManagementTool = applicationContext.getBean(NpmTool.class);
        }
        packageManagementTool.createFixFile(summaryFix, fileName);
    }
}

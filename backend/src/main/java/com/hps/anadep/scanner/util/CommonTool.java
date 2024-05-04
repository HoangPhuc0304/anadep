package com.hps.anadep.scanner.util;

import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.util.AnadepPropertyUtil;
import com.hps.anadep.model.util.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
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

    @Autowired
    private AnadepPropertyUtil anadepPropertyUtil;

    public ScanningResult getDependencies(boolean includeTransitive, Namespace namespace) throws Exception {
        String destinationDir = String.join("/", SCANNER_DIR, namespace.getPath());
        Ecosystem ecosystem = findEcosystem(namespace);
        if (ecosystem == null) {
            Path destinationPath = Path.of(destinationDir);
            String folderName;
            try (Stream<Path> stream = Files.list(destinationPath)) {
                folderName = stream.filter(Files::isDirectory)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .findFirst().get();
            }

            namespace.setPath(String.join("/", namespace.getPath(), folderName));
            ecosystem = findEcosystem(namespace);
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

    private Ecosystem findEcosystem(Namespace namespace) {
        String destinationDir = String.join("/", SCANNER_DIR, namespace.getPath());
        String ymlName = String.join("/", destinationDir, "%s.%s".formatted(AnadepPropertyUtil.FILENAME, AnadepPropertyUtil.YML_FORMAT));
        String yamlName = String.join("/", destinationDir, "%s.%s".formatted(AnadepPropertyUtil.FILENAME, AnadepPropertyUtil.YAML_FORMAT));
        File ymlFile = new File(ymlName);
        File yamlFile = new File(yamlName);

        Path destinationPath = Path.of(destinationDir);
        Ecosystem ecosystem = null;
        if (ymlFile.exists() && ymlFile.isFile()) {
            String path = anadepPropertyUtil.getScanPath(ymlFile);
            if (StringUtils.hasText(path)) {
                destinationPath = destinationPath.resolve(path);
                namespace.setManifestFilePath(path);
            }
        } else if (yamlFile.exists() && yamlFile.isFile()) {
            String path = anadepPropertyUtil.getScanPath(yamlFile);
            if (StringUtils.hasText(path)) {
                destinationPath = destinationPath.resolve(path);
                namespace.setManifestFilePath(path);
            }
        }

        File destinationFile = destinationPath.toFile();
        if (destinationFile.isFile()) {
            String manifestFile = destinationFile.getName().substring(destinationFile.getName().lastIndexOf(File.separatorChar) + 1);
            ecosystem = Ecosystem.getEcosystemFromPackageManagementFile(manifestFile);
            File destinationParentFile = destinationFile.getParentFile();
            namespace.setPath(destinationParentFile.getPath().substring(SCANNER_DIR.length() + 1));
        } else if (destinationFile.isDirectory()) {
            for (Ecosystem ecos : Ecosystem.values()) {
                if (Files.exists(Paths.get(destinationDir, ecos.getPackageManagementFile()))) {
                    ecosystem = ecos;
                    if (namespace.getManifestFilePath() == null) {
                        namespace.setManifestFilePath(ecos.getPackageManagementFile());
                    } else {
                        namespace.setManifestFilePath(String.join("/", namespace.getManifestFilePath(), ecos.getPackageManagementFile()));
                    }
                    break;
                }
            }
            namespace.setPath(destinationFile.getPath().substring(SCANNER_DIR.length() + 1));
        }

        return ecosystem;
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

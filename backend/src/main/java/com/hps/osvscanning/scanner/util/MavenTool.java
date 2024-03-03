package com.hps.osvscanning.scanner.util;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Ecosystem;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import static com.hps.osvscanning.scanner.util.FileStorage.*;

@Component
public class MavenTool implements PackageManagementTool {
    @Override
    public Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception {
        String storagePomXml = String.join("/", STORAGE_DIR, namespace, Ecosystem.MAVEN.getPackageManagementFile());
        String storageText = String.join("/", STORAGE_DIR, namespace, STORAGE_TEXT_FILE_NAME);
        Set<Library> libraries = new HashSet<>();

        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    String.format("./mvnw dependency:list --file %s -DoutputFile=%s -DexcludeTransitive=%b",
                            storagePomXml, STORAGE_TEXT_FILE_NAME, !includeTransitive));
        } catch (Exception exc) {
            process = Runtime.getRuntime().exec(
                    String.format("./mvnw.cmd dependency:list --file %s -DoutputFile=%s -DexcludeTransitive=%b",
                            storagePomXml, STORAGE_TEXT_FILE_NAME, !includeTransitive));
        }
        process.waitFor();

//        Process process;
//        try {
//            process = Runtime.getRuntime().exec(
//                    String.format("bash script/maven-get-dependencies.sh \"%s\" \"%s\" \"%b\"",
//                            storagePomXml, storageText, !includeTransitive));
//        } catch (Exception exc) {
//            throw new RuntimeException("There are something wrong with scanning");
//        }
//        process.waitFor();

        try (BufferedReader reader = new BufferedReader(new FileReader(storageText))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    String groupId = parts[0].strip();
                    String artifactId = parts[1].strip();
                    String version = parts[3].strip();
                    String name = String.format("%s:%s",groupId, artifactId);
                    libraries.add(new Library(name, version, Ecosystem.MAVEN.getOsvName()));
                }
            }
        }

        return libraries;
    }
}

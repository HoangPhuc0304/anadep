package com.hps.osvscanning.util;

import com.hps.osvscanning.model.Library;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MavenTool {
    private static final String STORAGE_POM_XML = "storage/pom.xml";
    private static final String STORAGE_TEXT = "storage/dependency.txt";

    public Set<Library> getDependencies(boolean includeTransitive) throws Exception {
        Set<Library> libraries = new HashSet<>();

        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    String.format("./mvnw dependency:list --file %s -DoutputFile=../%s -DexcludeTransitive=%b",
                            STORAGE_POM_XML, STORAGE_TEXT, !includeTransitive));
        } catch (Exception exc) {
            process = Runtime.getRuntime().exec(
                    String.format("./mvnw.cmd dependency:list --file %s -DoutputFile=../%s -DexcludeTransitive=%b ",
                            STORAGE_POM_XML, STORAGE_TEXT, !includeTransitive));
        }
        process.waitFor();

        try (BufferedReader reader = new BufferedReader(new FileReader(STORAGE_TEXT))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 4) {
                    String groupId = parts[0].strip();
                    String artifactId = parts[1].strip();
                    String version = parts[3].strip();
                    libraries.add(new Library(groupId,artifactId,version));
                }
            }
        }

        return libraries;
    }
}

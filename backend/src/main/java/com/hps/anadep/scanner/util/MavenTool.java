package com.hps.anadep.scanner.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.SummaryLibraryFix;
import com.hps.anadep.model.depgraph.Artifact;
import com.hps.anadep.model.depgraph.Dependency;
import com.hps.anadep.model.depgraph.Depgraph;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.enums.Resolution;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.util.Namespace;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hps.anadep.scanner.util.FileStorage.*;

@Component
@Slf4j
public class MavenTool implements PackageManagementTool {
    @Autowired
    private ObjectMapper objectMapper;

    private static final String MAVEN_FORMAT_ID = "%s:%s:%s";
    private static final String BEFORE_FILE_FORMAT = "storage/fix/before/%s";
    private static final String AFTER_FILE_FORMAT = "storage/fix/after/%s";
    private static final String DEPENDENCY_NAME_FORMAT = "%s:%s";

    @Override
    public ScanningResult getDependencies(boolean includeTransitive, Namespace namespace) throws Exception {
        String storagePackageDir = String.join("/", SCANNER_DIR, namespace.getPath());
        String storageJson = String.join("/", SCANNER_DIR, namespace.getPath(), STORAGE_JSON_FILE_NAME);
        Set<Library> libraries = new HashSet<>();

        processing(storagePackageDir, STORAGE_JSON_FILE_NAME);

        FileInputStream fis = new FileInputStream(String.join("/", storagePackageDir, Ecosystem.MAVEN.getPackageManagementFile()));
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(fis);
        fis.close();
        String groupId = model.getGroupId();
        String artifactId = model.getArtifactId();
        String version = model.getVersion();
        String packagingType = model.getPackaging();

        String id = String.format(MAVEN_FORMAT_ID, groupId, artifactId, packagingType);
        Depgraph depgraph = objectMapper.readValue(new File(storageJson), Depgraph.class);
        refactor(depgraph, id);
        List<Artifact> artifacts = depgraph.getArtifacts();

        if (!includeTransitive) {
            Map<String, Artifact> artifactMap = artifacts.stream().collect(Collectors.toMap(Artifact::getId, Function.identity()));
            List<String> ids = depgraph.getDependencies()
                    .stream().filter(d -> d.getFrom().equals(String.format(MAVEN_FORMAT_ID, groupId, artifactId, version)))
                    .map(Dependency::getTo).toList();
            artifacts = ids.stream().map(artifactMap::get).toList();
        }

        artifacts.forEach(artifact -> {
            libraries.add(new Library(
                    String.format("%s:%s", artifact.getGroupId(), artifact.getArtifactId()),
                    artifact.getVersion(),
                    Ecosystem.MAVEN.getOsvName()));
        });

        return ScanningResult.builder()
                .libraries(libraries)
                .dependencies(Set.copyOf(depgraph.getDependencies()))
                .projectName(depgraph.getGraphName())
                .ecosystem(Ecosystem.MAVEN.getOsvName())
                .path(namespace.getManifestFilePath())
                .libraryCount(libraries.size())
                .includeTransitive(includeTransitive)
                .build();
    }

    @Override
    public void createFixFile(SummaryFix summaryFix, String fileName) throws Exception {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        FileInputStream fis = new FileInputStream(BEFORE_FILE_FORMAT.formatted(fileName));
        Model model = reader.read(fis);
        fis.close();

        updateDependencyManagement(model, summaryFix);
        removeDependencyVersion(model, summaryFix);

        MavenXpp3Writer writer = new MavenXpp3Writer();
        FileUtils.createParentDirectories(new File(AFTER_FILE_FORMAT.formatted(fileName)));
        FileOutputStream fos = new FileOutputStream(AFTER_FILE_FORMAT.formatted(fileName));
        writer.write(fos, model);
        fos.close();
    }

    private void refactor(Depgraph depgraph, String id) {
        Map<String, String> idMap = new HashMap<>();
        depgraph.getArtifacts().forEach(d -> {
            idMap.put(d.getId(), String.format(MAVEN_FORMAT_ID, d.getGroupId(), d.getArtifactId(), d.getVersion()));
        });

        depgraph.setGraphName(idMap.get(id));

        for (Artifact artifact : depgraph.getArtifacts()) {
            artifact.setId(idMap.get(artifact.getId()));
        }

        for (Dependency dependency : depgraph.getDependencies()) {
            dependency.setFrom(idMap.get(dependency.getFrom()));
            if (dependency.getResolution().equalsIgnoreCase(Resolution.OMITTED_FOR_CONFLICT.name())) {
                String[] dep = dependency.getTo().split(":");
                assert dep.length == 3;
                dependency.setTo(String.format(MAVEN_FORMAT_ID, dep[0], dep[1], dependency.getVersion()));
                depgraph.getArtifacts().add(Artifact.builder()
                        .id(dependency.getTo())
                        .groupId(dep[0])
                        .artifactId(dep[1])
                        .version(dependency.getVersion())
                        .build());
            } else {
                dependency.setTo(idMap.get(dependency.getTo()));
            }
        }
    }

    private void processing(String storagePackageDir, String generatedFile) throws Exception {

        Process process;
        String command = String.format("mvn com.github.ferstl:depgraph-maven-plugin:4.0.3:graph -DgraphFormat=json -DshowVersions=true -DshowConflicts=true -DshowDuplicates=true -DoutputDirectory=. -DoutputFileName=%s", generatedFile);
        try {
            process = Runtime.getRuntime().exec(
                    new String[]{"/bin/sh", "-c", "cd " + storagePackageDir + " && " + command});
        } catch (Exception exc) {
            process = Runtime.getRuntime().exec(
                    new String[]{"cmd", "/c", "cd " + storagePackageDir + " && " + command});
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Future<?> future = executorService.submit(streamGobbler);
        int exitCode = process.waitFor();
        System.out.println("Exit code: " + exitCode);
    }

    private void updateDependencyManagement(Model model, SummaryFix summaryFix) {
        DependencyManagement dependencyManagement = model.getDependencyManagement();
        if (dependencyManagement == null) {
            model.setDependencyManagement(new DependencyManagement());
            for (SummaryLibraryFix fix : summaryFix.getLibs()) {
                addDependencyInDependencyManagement(fix, model);
            }
        } else {
            Map<String, org.apache.maven.model.Dependency> dependencyManagementMap = model.getDependencyManagement().getDependencies()
                    .stream().collect(Collectors.toMap(
                            d -> DEPENDENCY_NAME_FORMAT.formatted(d.getGroupId(), d.getArtifactId()),
                            Function.identity()
                    ));

            for (SummaryLibraryFix fix : summaryFix.getLibs()) {
                if (dependencyManagementMap.containsKey(fix.getName())) {
                    org.apache.maven.model.Dependency dependency = dependencyManagementMap.get(fix.getName());
                    if (StringUtils.hasText(fix.getFixedVersion())) {
                        dependency.setVersion(fix.getFixedVersion());
                    }
                } else {
                    addDependencyInDependencyManagement(fix, model);
                }
            }
        }
    }

    private void addDependencyInDependencyManagement(SummaryLibraryFix fix, Model model) {
        org.apache.maven.model.Dependency dependency = new org.apache.maven.model.Dependency();
        dependency.setGroupId(fix.getName().split(":")[0]);
        dependency.setArtifactId(fix.getName().split(":")[1]);
        if (StringUtils.hasText(fix.getFixedVersion())) {
            dependency.setVersion(fix.getFixedVersion());
            model.getDependencyManagement().addDependency(dependency);
        }
    }

    private void removeDependencyVersion(Model model, SummaryFix summaryFix) {
        Map<String, org.apache.maven.model.Dependency> dependencyMap = model.getDependencies().stream().collect(Collectors.toMap(
                d -> DEPENDENCY_NAME_FORMAT.formatted(d.getGroupId(), d.getArtifactId()),
                Function.identity()
        ));

        for (SummaryLibraryFix fix : summaryFix.getLibs()) {
            org.apache.maven.model.Dependency dependency = dependencyMap.get(fix.getName());
            if (dependency != null) {
                dependency.setVersion(null);
            }
        }
    }
}

package com.hps.anadepscheduler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadepscheduler.model.Ecosystem;
import com.hps.anadepscheduler.model.SummaryResult;
import com.hps.anadepscheduler.model.osv.Affected;
import com.hps.anadepscheduler.model.osv.LibraryEcosystem;
import com.hps.anadepscheduler.model.osv.Range;
import com.hps.anadepscheduler.model.osv.Vulnerability;
import com.hps.anadepscheduler.service.DynamodbService;
import com.hps.anadepscheduler.service.SchedulerService;
import com.hps.anadepscheduler.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private DynamodbService dynamodbService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String DEPENDENCY_TABLE_NAME = "Dependency";
    private final static String VUNERABILITY_TABLE_NAME = "Vulnerability";
    private final static String NAME = "Name";
    private final static String VERSION = "Version";
    private final static String VULN_IDS = "VulnIds";
    private final static String DATABASE_ID = "DatabaseId";
    private final static String CONTENT = "Content";
    private final static String UPDATED_AT = "UpdatedAt";
    private final static String RANGE_FIXED_VERSION_FORMAT = "[%s,%s)";
    private final static String RANGE_LAST_AFFECTED_VERSION_FORMAT = "[%s,%s]";
    private final static String ZIP_FILE = "all.zip";
    private final static int MAX_THREAD_POOL = 10;

    @Override
    public void update() throws Exception {

        for (Ecosystem ecosystem : Ecosystem.values()) {
            try {
                String filePath = String.join("/", ecosystem.getStorage(), ZIP_FILE);
                File file = new File(ecosystem.getStorage());
                if (!file.exists()) {
                    Files.createDirectories(Paths.get(ecosystem.getStorage()));
                }
                fileUtil.downloadZipFile(ecosystem.getUrl(), filePath);
                log.info("Completed download from url: {}", ecosystem.getUrl());
                ZipFile zipFile = new ZipFile(filePath);
                if (zipFile.isEncrypted()) {
                    throw new RuntimeException("Cannot extract zip file");
                }
                zipFile.extractAll(String.join("/", ecosystem.getStorage()));
                log.info("Completed extract file: {}", filePath);
                fileUtil.delete(filePath);

                File[] files = new File(ecosystem.getStorage()).listFiles();
                if (files != null) {
                    SummaryResult summaryResult = handlingVulnerability(files);
                    log.info("*** Summary Scanning ***");
                    log.info("Vulnerability count: {}", summaryResult.getVulnerabilityCount());
                    log.info("Updated Dependency count: {}", summaryResult.getDependencyCount());
                    log.info("Success file: {}", summaryResult.getSuccess());
                    log.info("Fail file: {}", summaryResult.getFail());
                    log.info("Execute Time (ms): {}", summaryResult.getExecuteTime());
                }
            } finally {
                fileUtil.clean(ecosystem.getStorage());
                log.info("Clean up storage: {}", ecosystem.getStorage());
            }
        }

//        File[] files = new File("src/main/resources/data/maven").listFiles();
//        if (files != null) {
//            SummaryResult summaryResult = handlingVulnerability(files);
//            log.info("*** Summary Scanning ***");
//            log.info("Vulnerability count: {}", summaryResult.getVulnerabilityCount());
//            log.info("Dependency count: {}", summaryResult.getDependencyCount());
//            log.info("Success file: {}", summaryResult.getSuccess());
//            log.info("Fail file: {}", summaryResult.getFail());
//            log.info("Execute Time (ms): {}", summaryResult.getExecuteTime());
//        File[] npmFiles = new File(NPM_DATA).listFiles();
//        File[] swiftFiles = new File(SWIFT_DATA).listFiles();
//
//        List<File> files = new ArrayList<>();
//        if (mavenFiles != null) {
//            files.addAll(Arrays.stream(mavenFiles).toList());
//        }
//        if (npmFiles != null) {
//            files.addAll(Arrays.stream(npmFiles).toList());
//        }
//        if (swiftFiles != null) {
//            files.addAll(Arrays.stream(swiftFiles).toList());
//        }
//
//        fileUtil.downloadZipFile();

//        handlingVulnerability(files);
    }

    private SummaryResult handlingVulnerability(File[] files) {
        SummaryResult summaryResult = new SummaryResult();
        long executedTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
        List<CompletableFuture<SummaryResult>> completableFutures = Arrays.stream(files)
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        log.info("Handling file: {}, with size {} bytes", file.getName(), file.length());
                        return handlingVulnerability(file);
                    } catch (Exception exc) {
                        return null;
                    }
                }, executorService))
                .toList();
        List<SummaryResult> summaryResults = completableFutures.stream()
                .map(CompletableFuture::join)
                .toList();

        summaryResults.forEach(r -> {
            if (r != null) {
                summaryResult.setVulnerabilityCount(summaryResult.getVulnerabilityCount() + r.getVulnerabilityCount());
                summaryResult.setDependencyCount(summaryResult.getDependencyCount() + r.getDependencyCount());
                summaryResult.setSuccess(summaryResult.getSuccess() + r.getSuccess());
                summaryResult.setFail(summaryResult.getFail() + r.getFail());
            }
        });

        executorService.shutdown();

        summaryResult.setExecuteTime(System.currentTimeMillis() - executedTime);
        return summaryResult;
    }

    private SummaryResult handlingVulnerability(File file) {
        SummaryResult summaryResult = new SummaryResult();
        try {
            if (file.isDirectory()) {
                throw new RuntimeException("Only support file");
            }

            Vulnerability vuln = objectMapper.readValue(file, Vulnerability.class);
            saveItem(vuln);
            summaryResult.setVulnerabilityCount(summaryResult.getVulnerabilityCount() + 1);

            for (Affected affected : vuln.getAffected()) {
                try {
                    LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
                    if (CollectionUtils.isEmpty(affected.getRanges())) {
                        handlingVulnerabilityUnFixed(libraryEcosystem, affected.getVersions(), vuln);
                    } else {
                        Range firstRange = affected.getRanges().get(0);
                        if (firstRange.getEvents().size() < 2 ||
                                !StringUtils.hasText(firstRange.getEvents().get(0).getIntroduced()) ||
                                (!StringUtils.hasText(firstRange.getEvents().get(1).getFixed()) &&
                                        !StringUtils.hasText(firstRange.getEvents().get(1).getLastAffected()))) {
                            handlingVulnerabilityUnFixed(libraryEcosystem, affected.getVersions(), vuln);
                        } else {
                            //                            Semver introducedVersion = new Semver(range.getEvents().get(0).getIntroduced());
                            //                            Semver fixVersion = new Semver(range.getEvents().get(1).getFixed());
                            String version = null;
                            if (StringUtils.hasText(firstRange.getEvents().get(1).getFixed())) {
                                version = String.format(RANGE_FIXED_VERSION_FORMAT, firstRange.getEvents().get(0).getIntroduced(), firstRange.getEvents().get(1).getFixed());
                            } else if (StringUtils.hasText(firstRange.getEvents().get(1).getLastAffected())) {
                                version = String.format(RANGE_LAST_AFFECTED_VERSION_FORMAT, firstRange.getEvents().get(0).getIntroduced(), firstRange.getEvents().get(1).getLastAffected());
                            }

                            Map<String, AttributeValue> key = Map.of(
                                    NAME, AttributeValue.fromS(String.format("%s:%s", libraryEcosystem.getEcosystem(), libraryEcosystem.getName())),
                                    VERSION, AttributeValue.fromS(version)
                            );
                            Map<String, AttributeValue> item = dynamodbService.getItem(DEPENDENCY_TABLE_NAME, key);
                            List<String> vulnIds = new ArrayList<>();
                            if (item.isEmpty()) {
                                vulnIds.add(vuln.getId());
                            } else {
                                vulnIds = new ArrayList<>(item.get(VULN_IDS).ss());
                                if (vulnIds.contains(vuln.getId())) {
                                    continue;
                                }
                                vulnIds.add(vuln.getId());
                            }
                            saveItem(libraryEcosystem, version, vulnIds);
                        }
                    }
                    summaryResult.setDependencyCount(summaryResult.getDependencyCount() + 1);
                } catch (Exception exc) {
                    log.error("Error dependency {} in file {}, should be skip with message: {}",
                            affected.getLibraryEcosystem().getName(), file.getName(), exc.getMessage());
                }
            }
            summaryResult.setSuccess(summaryResult.getSuccess() + 1);
            log.info("Completing file: {}", file.getName());
            return summaryResult;
        } catch (Exception exc) {
            log.error("Failing file: {}, with message: {}", file.getName(), exc.getMessage());
            summaryResult.setFail(summaryResult.getFail() + 1);
            return summaryResult;
        }
    }

    private void handlingVulnerabilityUnFixed(LibraryEcosystem libraryEcosystem, List<String> versions, Vulnerability vuln) {
        String versionStr = "{}";
        if (!CollectionUtils.isEmpty(versions)) {
            versionStr = versions.stream().collect(Collectors.joining(",", "{", "}"));
        }
        Map<String, AttributeValue> item = Map.of(
                NAME, AttributeValue.fromS(String.format("%s:%s", libraryEcosystem.getEcosystem(), libraryEcosystem.getName())),
                VERSION, AttributeValue.fromS(versionStr),
                VULN_IDS, AttributeValue.fromSs(List.of(vuln.getId()))
        );
        dynamodbService.putItem(DEPENDENCY_TABLE_NAME, item);
    }

    private void saveItem(Vulnerability vuln) throws JsonProcessingException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        Map<String, AttributeValue> item = Map.of(
                DATABASE_ID, AttributeValue.fromS(vuln.getId()),
                CONTENT, AttributeValue.fromS(objectMapper.writeValueAsString(vuln)),
                UPDATED_AT, AttributeValue.fromS(currentDateTime)
        );
        dynamodbService.putItem(VUNERABILITY_TABLE_NAME, item);
    }

    private void saveItem(LibraryEcosystem libraryEcosystem, String version,List<String> vulnIds) {
        Map<String, AttributeValue> item = Map.of(
                NAME, AttributeValue.fromS(String.format("%s:%s", libraryEcosystem.getEcosystem(), libraryEcosystem.getName())),
                VERSION, AttributeValue.fromS(version),
                VULN_IDS, AttributeValue.fromSs(vulnIds)
        );
        dynamodbService.putItem(DEPENDENCY_TABLE_NAME, item);
    }
}

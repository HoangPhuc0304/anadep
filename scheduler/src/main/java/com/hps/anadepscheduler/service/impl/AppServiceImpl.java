package com.hps.anadepscheduler.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadepscheduler.exception.NotFoundException;
import com.hps.anadepscheduler.model.Ecosystem;
import com.hps.anadepscheduler.model.Library;
import com.hps.anadepscheduler.model.LibraryScan;
import com.hps.anadepscheduler.model.osv.*;
import com.hps.anadepscheduler.service.AppService;
import com.hps.anadepscheduler.service.DynamodbService;
import com.hps.anadepscheduler.util.SemverUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.KeysAndAttributes;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AppServiceImpl implements AppService {
    @Autowired
    private DynamodbService dynamodbService;
    @Autowired
    private ObjectMapper objectMapper;

    private final static String DEPENDENCY_TABLE_NAME = "Dependency";
    private final static String VUNERABILITY_TABLE_NAME = "Vulnerability";
    private final static String NAME = "Name";
    private final static String VERSION = "Version";
    private final static String VULN_IDS = "VulnIds";
    private final static String DATABASE_ID = "DatabaseId";
    private final static String CONTENT = "Content";

    @Override
    public VulnerabilityOSVResponse query(LibraryOSVRequest libraryOSVRequest) {
        String name = libraryOSVRequest.getLibraryEcosystem().getName();
        String version = libraryOSVRequest.getVersion();
        Ecosystem ecosystem = Ecosystem.getEcosystem(libraryOSVRequest.getLibraryEcosystem().getEcosystem());

        List<Map<String, AttributeValue>> items = dynamodbService.query(
                DEPENDENCY_TABLE_NAME,
                "#name = :name",
                Map.of("#name", NAME),
                Map.of(":name", AttributeValue.fromS(String.format("%s:%s", ecosystem.getOsvName(), name)))
        );

        Set<String> vulnIds = new HashSet<>();
        for (Map<String, AttributeValue> item : items) {
            List<String> ids = item.get(VULN_IDS).ss();

            if (!StringUtils.hasText(version)) {
                vulnIds.addAll(ids);
                continue;
            }

            String versionRange = item.get(VERSION).s();
            if (!StringUtils.hasText(versionRange) || versionRange.length() < 2) {
                continue;
            }

            if (versionRange.startsWith("[") && versionRange.endsWith(")")) {
                String[] range = versionRange.substring(1, versionRange.length() - 1).split(",");
                if (range.length != 2) {
                    continue;
                }
                SemverUtil semver = new SemverUtil(version, ecosystem);
                if (semver.isGreaterThanOrEqualTo(range[0].strip()) && semver.isLowerThan(range[1].strip())) {
                    vulnIds.addAll(ids);
                }

            } else if (versionRange.startsWith("[") && versionRange.endsWith("]")) {
                String[] range = versionRange.substring(1, versionRange.length() - 1).split(",");
                if (range.length != 2) {
                    continue;
                }
                SemverUtil semver = new SemverUtil(version, ecosystem);
                if (semver.isGreaterThanOrEqualTo(range[0].strip()) && semver.isLowerThanOrEqualTo(range[1].strip())) {
                    vulnIds.addAll(ids);
                }

            } else if (versionRange.startsWith("{") && versionRange.endsWith("}")) {
                if (versionRange.length() > 2) {
                    String[] range = versionRange.substring(1, versionRange.length() - 1).split(",");
                    for (String v : range) {
                        if (v.strip().equals(version)) {
                            vulnIds.addAll(ids);
                            break;
                        }
                    }
                } else {
                    vulnIds.addAll(ids);
                }
            }
        }

        if (CollectionUtils.isEmpty(vulnIds)) {
            return VulnerabilityOSVResponse.builder()
                    .vulns(Collections.emptyList())
                    .build();
        }

        KeysAndAttributes keys = KeysAndAttributes.builder()
                .keys(vulnIds.stream().map(id -> Map.of(
                        DATABASE_ID,
                        AttributeValue.fromS(id)
                )).toList())
                .build();
        List<Map<String, AttributeValue>> vulnItems = dynamodbService.getBatchItem(
                VUNERABILITY_TABLE_NAME,
                keys
        ).get(VUNERABILITY_TABLE_NAME);
        List<Vulnerability> vulns = new ArrayList<>();
        if (!CollectionUtils.isEmpty(vulnItems)) {
            vulns = vulnItems.stream().map(item -> {
                try {
                    return objectMapper.readValue(item.get(CONTENT).s(), Vulnerability.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }).toList();
        }

        return VulnerabilityOSVResponse.builder()
                .vulns(vulns)
                .build();
    }

    @Override
    public VulnerabilityOSVBatchResponse query(LibraryOSVBatchRequest libraryOSVBatchRequest) {
        if (CollectionUtils.isEmpty(libraryOSVBatchRequest.getQueries())) {
            return new VulnerabilityOSVBatchResponse();
        }

        try {
            ExecutorService executorService = Executors.newFixedThreadPool(libraryOSVBatchRequest.getQueries().size());
            List<CompletableFuture<LibraryScan>> completableFutures = libraryOSVBatchRequest.getQueries().stream()
                    .map(library -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return LibraryScan.builder()
                                    .info(Library.builder()
                                            .name(library.getLibraryEcosystem().getName())
                                            .version(library.getVersion())
                                            .ecosystem(library.getLibraryEcosystem().getEcosystem())
                                            .build())
                                    .vulns(query(library).getVulns())
                                    .build();
                        } catch (Exception e) {
                            log.error("Error retrieving {}:{} of ecosystem {} with message: {}",
                                    library.getLibraryEcosystem().getName(),
                                    library.getVersion(),
                                    library.getLibraryEcosystem().getEcosystem(),
                                    e.getMessage()
                            );
                            return LibraryScan.builder()
                                    .info(Library.builder()
                                            .name(library.getLibraryEcosystem().getName())
                                            .version(library.getVersion())
                                            .ecosystem(library.getLibraryEcosystem().getEcosystem())
                                            .build())
                                    .vulns(Collections.emptyList())
                                    .build();
                        }
                    }, executorService))
                    .toList();

            List<LibraryScan> libs = completableFutures.stream()
                    .map(CompletableFuture::join)
                    .toList();
            executorService.shutdown();
            return VulnerabilityOSVBatchResponse.builder()
                    .libs(libs)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new VulnerabilityOSVBatchResponse();
        }
    }

    @Override
    public Vulnerability query(String databasedId) {
        Map<String, AttributeValue> key = Map.of(
                DATABASE_ID, AttributeValue.fromS(databasedId)
        );
        Map<String, AttributeValue> item = dynamodbService.getItem(VUNERABILITY_TABLE_NAME, key);
        if (item.isEmpty()) {
            throw new NotFoundException(String.format("Not found vulnerability with id: %s", databasedId));
        }
        String vulnStr = item.get(CONTENT).s();
        try {
            return objectMapper.readValue(vulnStr, Vulnerability.class);
        } catch (Exception exc) {
            throw new RuntimeException(exc.getMessage());
        }
    }
}

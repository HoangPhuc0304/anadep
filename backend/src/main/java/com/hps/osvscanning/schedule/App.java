//package com.hps.osvscanning.schedule;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.hps.osvscanning.model.LibraryEcosystem;
//import com.hps.osvscanning.model.enums.RangeType;
//import com.hps.osvscanning.model.mongo.MavenEcosystem;
//import com.hps.osvscanning.model.osv.*;
//import com.hps.osvscanning.model.response.LibraryScan;
//import com.hps.osvscanning.schedule.repository.MavenRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.StringUtils;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Configuration
//@Slf4j
//public class App implements CommandLineRunner {
//    @Autowired
//    private MavenRepository mavenRepository;
//    @Autowired
//    private ObjectMapper objectMapper;
//    private final static String MAVEN_DATA = "src/main/resources/data/maven";
//    private final static String RANGE_VERSION_FORMAT = "[%s,%s)";
//
//    @Override
//    public void run(String... args) throws Exception {
//        log.info("Starting to traverse {}", MAVEN_DATA);
//        File dir = new File(MAVEN_DATA);
//        File[] files = dir.listFiles();
//
//        long start = System.currentTimeMillis();
//        assert files != null;
//
//        List<Vulnerability> vulnerabilities = handlingVulnerability(files);
//        List<MavenEcosystem> ecosystems = handlingEcosystem(vulnerabilities);
//        ExecutorService executorService = Executors.newFixedThreadPool(files.length);
//        List<CompletableFuture<MavenEcosystem>> completableFutures = Arrays.stream(files)
//                .map(file -> CompletableFuture.supplyAsync(() -> handlingEcosystem(file)))
//                .toList();
//
//        List<MavenEcosystem> mavenEcosystems = completableFutures.stream()
//                .map(CompletableFuture::join)
//                .toList();
//
//        executorService.shutdown();
//
////        for(File jsonFile : Objects.requireNonNull(dir.listFiles())) {
////            try {
////                log.info("Handling file: data/maven/{}", jsonFile.getName());
////                if (jsonFile.isDirectory()) {
////                    continue;
////                }
////                Vulnerability vuln = objectMapper.readValue(jsonFile, Vulnerability.class);
////                for (Affected affected : vuln.getAffected()) {
////                    Range firstRange = CollectionUtils.isEmpty(affected.getRanges()) ? new Range() : affected.getRanges().get(0);
////                    RangeType rangeType = RangeType.getRangeType(firstRange.getType());
////                    if (rangeType.equals(RangeType.ECOSYSTEM)) {
////                        LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
////                        List<String> versions = affected.getVersions();
////                        for (String version : versions) {
////                            MavenEcosystem mavenEcosystem = (MavenEcosystem) mavenRepository.findCommonEcosystemByNameAndEcosystemAndTypeAndVersion(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version);
////                            if (mavenEcosystem == null) {
////                                mavenEcosystem = generateMavenEcosystem(libraryEcosystem, version, vuln);
////                            } else {
////                                updateMavenEcosystem(mavenEcosystem, vuln);
////                            }
////                            mavenRepository.save(mavenEcosystem);
////                        }
////                    } else {
////                        LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
////                        for (Range range : affected.getRanges()) {
////                            if (!StringUtils.hasText(range.getEvents().get(0).getIntroduced()) || !StringUtils.hasText(range.getEvents().get(1).getFixed())) {
////                                continue;
////                            }
////                            String version = String.format(RANGE_VERSION_FORMAT, range.getEvents().get(0).getIntroduced(), range.getEvents().get(1).getFixed());
////                            MavenEcosystem mavenEcosystem = (MavenEcosystem) mavenRepository.findCommonEcosystemByNameAndEcosystemAndTypeAndVersion(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version);
////                            if (mavenEcosystem == null) {
////                                mavenEcosystem = generateMavenEcosystem(libraryEcosystem, version, vuln);
////                            } else {
////                                updateMavenEcosystem(mavenEcosystem, vuln);
////                            }
////                            mavenRepository.save(mavenEcosystem);
////                        }
////                    }
////                }
////                log.info("Completing file: data/maven/{}", jsonFile.getName());
////            } catch (Exception exc) {
////                log.error("Failing file: data/maven/{}", jsonFile.getName());
////            }
////        }
//        log.info("Took: {}", System.currentTimeMillis() - start);
//        log.info("Finishing to saving with {} records", mavenEcosystems.size());
//    }
//
//    private List<Vulnerability> handlingVulnerability(File[] files) {
//        for(File jsonFile : files) {
//            try {
//                log.info("Handling file: {}/{}",MAVEN_DATA, jsonFile.getName());
//                if (jsonFile.isDirectory()) {
//                    continue;
//                }
//                Vulnerability vuln = objectMapper.readValue(jsonFile, Vulnerability.class);
//
//                for (Affected affected : vuln.getAffected()) {
//                    Range firstRange = CollectionUtils.isEmpty(affected.getRanges()) ? new Range() : affected.getRanges().get(0);
//                    RangeType rangeType = RangeType.getRangeType(firstRange.getType());
//                    if (rangeType.equals(RangeType.ECOSYSTEM)) {
//                        LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
//                        List<String> versions = affected.getVersions();
//                        for (String version : versions) {
//                            MavenEcosystem mavenEcosystem = (MavenEcosystem) mavenRepository.findCommonEcosystemByNameAndEcosystemAndTypeAndVersion(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version);
//                            if (mavenEcosystem == null) {
//                                mavenEcosystem = generateMavenEcosystem(libraryEcosystem, version, vuln);
//                            } else {
//                                updateMavenEcosystem(mavenEcosystem, vuln);
//                            }
//                            mavenRepository.save(mavenEcosystem);
//                        }
//                    } else {
//                        LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
//                        for (Range range : affected.getRanges()) {
//                            if (!StringUtils.hasText(range.getEvents().get(0).getIntroduced()) || !StringUtils.hasText(range.getEvents().get(1).getFixed())) {
//                                continue;
//                            }
//                            String version = String.format(RANGE_VERSION_FORMAT, range.getEvents().get(0).getIntroduced(), range.getEvents().get(1).getFixed());
//                            MavenEcosystem mavenEcosystem = (MavenEcosystem) mavenRepository.findCommonEcosystemByNameAndEcosystemAndTypeAndVersion(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version);
//                            if (mavenEcosystem == null) {
//                                mavenEcosystem = generateMavenEcosystem(libraryEcosystem, version, vuln);
//                            } else {
//                                updateMavenEcosystem(mavenEcosystem, vuln);
//                            }
//                            mavenRepository.save(mavenEcosystem);
//                        }
//                    }
//                }
//                log.info("Completing file: data/maven/{}", jsonFile.getName());
//            } catch (Exception exc) {
//                log.error("Failing file: data/maven/{}", jsonFile.getName());
//            }
//        }
//    }
//
//    private MavenEcosystem handlingEcosystem(Vulnerability vuln) {
//        try {
////            log.info("Handling file: data/maven/{}", jsonFile.getName());
////            if (jsonFile.isDirectory()) {
////                return null;
////            }
////            Vulnerability vuln = objectMapper.readValue(jsonFile, Vulnerability.class);
//            for (Affected affected : vuln.getAffected()) {
//                Range firstRange = CollectionUtils.isEmpty(affected.getRanges()) ? new Range() : affected.getRanges().get(0);
//                RangeType rangeType = RangeType.getRangeType(firstRange.getType());
//                if (rangeType.equals(RangeType.ECOSYSTEM)) {
//                    LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
//                    List<String> versions = affected.getVersions();
//                    for (String version : versions) {
//                        MavenEcosystem mavenEcosystem = mavenRepository.findCommonEcosystemByNameAndEcosystemAndTypeAndVersion(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version);
//                        if (mavenEcosystem == null) {
//                            mavenEcosystem = generateMavenEcosystem(libraryEcosystem, version, vuln);
//                        } else {
//                            updateMavenEcosystem(mavenEcosystem, vuln);
//                        }
//                        return mavenRepository.save(mavenEcosystem);
//                    }
//                } else {
//                    LibraryEcosystem libraryEcosystem = affected.getLibraryEcosystem();
//                    for (Range range : affected.getRanges()) {
//                        if (!StringUtils.hasText(range.getEvents().get(0).getIntroduced()) || !StringUtils.hasText(range.getEvents().get(1).getFixed())) {
//                            continue;
//                        }
//                        String version = String.format(RANGE_VERSION_FORMAT, range.getEvents().get(0).getIntroduced(), range.getEvents().get(1).getFixed());
//                        MavenEcosystem mavenEcosystem = mavenRepository.findCommonEcosystemByNameAndEcosystemAndTypeAndVersion(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version);
//                        if (mavenEcosystem == null) {
//                            mavenEcosystem = generateMavenEcosystem(libraryEcosystem, version, vuln);
//                        } else {
//                            updateMavenEcosystem(mavenEcosystem, vuln);
//                        }
//                        return mavenRepository.save(mavenEcosystem);
//                    }
//                }
//            }
////            log.info("Completing file: data/maven/{}", jsonFile.getName());
//        } catch (Exception exc) {
//            log.error("Failing to save: {}", vuln);
//        }
//        return null;
//    }
//
//    private void updateMavenEcosystem(MavenEcosystem mavenEcosystem, Vulnerability vuln) {
//        VulnerabilityOSVResponse osvResponse = mavenEcosystem.getData();
//        List<Vulnerability> vulns = osvResponse.getVulns();
//        boolean isReplace = false;
//        for(int i=0;i< vulns.size();i++) {
//            if (vulns.get(i).getId().equals(vuln.getId())) {
//                vulns.set(i, vuln);
//                isReplace = true;
//                break;
//            }
//        }
//        if (!isReplace) {
//            vulns.add(vuln);
//        }
//        osvResponse.setVulns(vulns);
//        mavenEcosystem.setData(osvResponse);
//        log.info("Updating to cloud: {}", mavenEcosystem);
//    }
//
//    private MavenEcosystem generateMavenEcosystem(LibraryEcosystem libraryEcosystem, String version, Vulnerability vuln) {
//        List<Vulnerability> vulns = new ArrayList<>();
//        vulns.add(vuln);
//        VulnerabilityOSVResponse osvResponse = new VulnerabilityOSVResponse(vulns);
//        MavenEcosystem mavenEcosystem = new MavenEcosystem(libraryEcosystem.getName(), libraryEcosystem.getEcosystem(), RangeType.ECOSYSTEM.name(), version, osvResponse);
//        log.info("Inserting to cloud: {}", mavenEcosystem);
//        return mavenEcosystem;
//    }
//}

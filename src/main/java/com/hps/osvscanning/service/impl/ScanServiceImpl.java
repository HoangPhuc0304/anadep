package com.hps.osvscanning.service.impl;

import com.hps.osvscanning.model.*;
import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.mvn.ArtifactDoc;
import com.hps.osvscanning.model.mvn.LibraryBulk;
import com.hps.osvscanning.model.osv.*;
import com.hps.osvscanning.service.FileService;
import com.hps.osvscanning.service.MavenService;
import com.hps.osvscanning.service.OsvService;
import com.hps.osvscanning.service.ScanService;
import com.hps.osvscanning.util.MavenTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
public class ScanServiceImpl implements ScanService {
    @Autowired
    private MavenService mavenService;
    @Autowired
    private OsvService osvService;
    @Autowired
    private FileService fileService;
    @Autowired
    private MavenTool mavenTool;
    @Value("${library.custom-fix.enable}")
    private Boolean enabledCustomFix;
    @Value("${library.available-fix.enable}")
    private Boolean enabledAvailableFix;
    private static final int MAX_LIBRARY_RECORDS = 200;

    @Override
    public VulnerabilityBulk retrieve(Library libraryInfo) {
        VulnerabilityBulk vulnerabilityBulk = new VulnerabilityBulk();
        long start = System.currentTimeMillis();
        VulnerabilityList vulnerabilityList = osvService.findVulnerabilities(libraryInfo);
        if (CollectionUtils.isEmpty(vulnerabilityList.getVulns())) {
            return new VulnerabilityBulk(System.currentTimeMillis() - start);
        }
        List<VulnerabilityFixed> vulnerabilityFixedList = new ArrayList<>();

        for (Vulnerability vul : vulnerabilityList.getVulns()) {
            Fix fix = findFix(vul, libraryInfo);
            if (fix == null) {
                vulnerabilityFixedList.add(new VulnerabilityFixed(vul, null));
                continue;
            }
            vulnerabilityFixedList.add(new VulnerabilityFixed(vul, fix));
        }

        vulnerabilityBulk.setInfo(vulnerabilityFixedList);
        vulnerabilityBulk.setCount(vulnerabilityFixedList.size());
        vulnerabilityBulk.setResponseTime(System.currentTimeMillis() - start);

        return vulnerabilityBulk;
    }

    @Override
    public VulnerabilityBulk scan(MultipartFile file) {
        List<VulnerabilityFixed> infoList = new ArrayList<>();
        long responseTime = 0;
        try {
            fileService.save(file);
            Set<Library> libraries = mavenTool.getDependencies(true);
            for (Library library : libraries) {
                VulnerabilityBulk vulnerabilityBulk = retrieve(library);
                if (!CollectionUtils.isEmpty(vulnerabilityBulk.getInfo())) {
                    infoList.addAll(vulnerabilityBulk.getInfo());
                }
                responseTime += vulnerabilityBulk.getResponseTime();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            fileService.clean();
        }

        return new VulnerabilityBulk(infoList, infoList.size(), responseTime);
    }

    private Fix findFix(Vulnerability vul, Library libraryInfo) {
        Set<String> effectedVersions = new HashSet<>();
        for (Affected affected : vul.getAffected()) {
            if (CollectionUtils.isEmpty(affected.getVersions())) {
                continue;
            }
            effectedVersions.addAll(affected.getVersions());
            if (!affected.getVersions().contains(libraryInfo.getVersion())) {
                continue;
            }
            
            if (!enabledAvailableFix) {
                continue;
            }
            for (Range range : affected.getRanges()) {
                for (Event event : range.getEvents()) {
                    if (StringUtils.hasText(event.getFixed())) {
                        return new Fix(
                                new Library(
                                        libraryInfo.getGroupId(),
                                        libraryInfo.getArtifactId(),
                                        event.getFixed()
                                )
                        );
                    }
                }
            }
        }
        if (enabledCustomFix) {
            Fix fix = findFixByCustom(libraryInfo, effectedVersions);
            if (fix != null) {
                VulnerabilityList vulnerabilityList = osvService.findVulnerabilities(fix.getLibraryInfo());
                if (vulnerabilityList.getVulns() == null) {
                    return fix;
                }
            }
        }
        return null;
    }

    private Fix findFixByCustom(Library libraryInfo, Set<String> versions) {
        if (CollectionUtils.isEmpty(versions)) {
            return null;
        }

        int allVersions = mavenService.getLibraryCount(libraryInfo);
        if (allVersions > MAX_LIBRARY_RECORDS) {
            allVersions = MAX_LIBRARY_RECORDS;
        }
        LibraryBulk libraryBulk = mavenService.findLibrary(libraryInfo, allVersions);
        List<ArtifactDoc> docs = libraryBulk.getResponse().getDocs();
        int idx = docs.size();
        for (String version : versions) {
            for (int i = 0; i < idx; i++) {
                if (docs.get(i).getV().equals(version)) {
                    idx = i;
                    break;
                }
            }
        }

        if (idx == 0 || idx == docs.size()) {
            return null;
        }
        for (int i = idx - 1; i >= 0; i--) {
            if (!versions.contains(docs.get(i).getV())) {
                return new Fix(
                        new Library(
                                libraryInfo.getGroupId(),
                                libraryInfo.getArtifactId(),
                                docs.get(i).getV()
                        )
                );
            }
        }
        return null;
    }
}

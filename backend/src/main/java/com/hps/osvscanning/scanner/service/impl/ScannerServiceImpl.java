package com.hps.osvscanning.scanner.service.impl;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.enums.Compress;
import com.hps.osvscanning.model.enums.Ecosystem;
import com.hps.osvscanning.scanner.service.FileService;
import com.hps.osvscanning.scanner.service.ScannerService;
import com.hps.osvscanning.scanner.util.CommonTool;
import com.hps.osvscanning.scanner.util.MavenTool;
import com.hps.osvscanning.scanner.util.NpmTool;
import com.hps.osvscanning.scanner.util.PackageManagementTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Collections;
import java.util.Set;

@Service
public class ScannerServiceImpl implements ScannerService {
    @Autowired
    private FileService fileService;

    @Override
    public Set<Library> scan(MultipartFile file, boolean includeTransitive) throws Exception {
        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        String folderName = filename.substring(0, filename.lastIndexOf("."));
        if (extension.equals(Compress.ZIP.getName())) {
            String namespace = Compress.ZIP.getName() + "-" + Instant.now().toEpochMilli();
            PackageManagementTool packageManagementTool = new CommonTool();
            try {
                fileService.save(file, filename, namespace);
                Set<Library> libraries = packageManagementTool.getDependencies(includeTransitive, String.join("/", namespace, folderName));
                return libraries;
            } finally {
                fileService.clean(namespace);
            }
        } else {
            Ecosystem ecosystem = Ecosystem.getEcosystemFromPackageManagementFile(filename);
            PackageManagementTool packageManagementTool = null;
            String namespace = ecosystem.getOsvName() + "-" + Instant.now().toEpochMilli();
            switch (ecosystem) {
                case MAVEN -> packageManagementTool = new MavenTool();
                case NPM -> packageManagementTool = new NpmTool();
                default -> {
                    return Collections.emptySet();
                }
            }
            try {
                fileService.save(file, ecosystem.getPackageManagementFile(), namespace);
                Set<Library> libraries = packageManagementTool.getDependencies(includeTransitive, namespace);
                return libraries;
            } finally {
                fileService.clean(namespace);
            }
        }
    }
}

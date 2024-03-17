package com.hps.anadep.scanner.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.Library;
import com.hps.anadep.model.enums.Compress;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.scanner.service.FileService;
import com.hps.anadep.scanner.service.ScannerService;
import com.hps.anadep.scanner.util.CommonTool;
import com.hps.anadep.scanner.util.MavenTool;
import com.hps.anadep.scanner.util.NpmTool;
import com.hps.anadep.scanner.util.PackageManagementTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.hps.anadep.scanner.util.FileStorage.SCANNER_DIR;

@Service
public class ScannerServiceImpl implements ScannerService {
    @Autowired
    private FileService fileService;

    @Autowired
    private ApplicationContext applicationContext;

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
            PackageManagementTool packageManagementTool = applicationContext.getBean(CommonTool.class);
            try {
                fileService.save(file, filename, namespace);
                Set<Library> libraries = packageManagementTool.getDependencies(includeTransitive, String.join("/", namespace, folderName));
                return libraries;
            } finally {
                fileService.clean(String.join("/", SCANNER_DIR, namespace));
            }
        } else {
            Ecosystem ecosystem = Ecosystem.getEcosystemFromPackageManagementFile(filename);
            PackageManagementTool packageManagementTool;
            String namespace = ecosystem.getOsvName() + "-" + Instant.now().toEpochMilli();
            switch (ecosystem) {
                case MAVEN -> packageManagementTool = applicationContext.getBean(MavenTool.class);
                case NPM -> packageManagementTool = applicationContext.getBean(NpmTool.class);
                default -> {
                    return Collections.emptySet();
                }
            }
            try {
                fileService.save(file, ecosystem.getPackageManagementFile(), namespace);
                Set<Library> libraries = packageManagementTool.getDependencies(includeTransitive, namespace);
                return libraries;
            } finally {
                fileService.clean(String.join("/", SCANNER_DIR, namespace));
            }
        }
    }

    @Override
    public Map<Library, String> scanToMap(MultipartFile file) throws Exception {
        String filename = file.getOriginalFilename();
        if (!StringUtils.hasText(filename)) {
            return null;
        }
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        String folderName = filename.substring(0, filename.lastIndexOf("."));
        if (extension.equals(Compress.ZIP.getName())) {
            String namespace = Compress.ZIP.getName() + "-" + Instant.now().toEpochMilli();
            PackageManagementTool packageManagementTool = applicationContext.getBean(CommonTool.class);
            try {
                fileService.save(file, filename, namespace);
                Map<Library, String> map = packageManagementTool.getDependencyMap(String.join("/", namespace, folderName));
                return map;
            } finally {
                fileService.clean(String.join("/", SCANNER_DIR, namespace));
            }
        } else {
            Ecosystem ecosystem = Ecosystem.getEcosystemFromPackageManagementFile(filename);
            PackageManagementTool packageManagementTool;
            String namespace = ecosystem.getOsvName() + "-" + Instant.now().toEpochMilli();
            switch (ecosystem) {
                case MAVEN -> packageManagementTool = applicationContext.getBean(MavenTool.class);
                case NPM -> packageManagementTool = applicationContext.getBean(NpmTool.class);
                default -> {
                    return Collections.emptyMap();
                }
            }
            try {
                fileService.save(file, ecosystem.getPackageManagementFile(), namespace);
                Map<Library, String> map = packageManagementTool.getDependencyMap(String.join("/", namespace, folderName));
                return map;
            } finally {
                fileService.clean(String.join("/", SCANNER_DIR, namespace));
            }
        }
    }

    @Override
    public boolean isUseLibrary(Map.Entry<Library, String> entry, Library lib) throws JsonProcessingException {
        Ecosystem ecosystem = Ecosystem.getEcosystem(lib.getEcosystem());
        PackageManagementTool packageManagementTool;
        switch (ecosystem) {
            case MAVEN -> packageManagementTool = applicationContext.getBean(MavenTool.class);
            case NPM -> packageManagementTool = applicationContext.getBean(NpmTool.class);
            default -> {
                return false;
            }
        }
        return packageManagementTool.isUseLibrary(entry, lib);
    }
}

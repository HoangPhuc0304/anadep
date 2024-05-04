package com.hps.anadep.scanner.service.impl;

import com.hps.anadep.model.enums.Compress;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.util.Namespace;
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

import static com.hps.anadep.scanner.util.FileStorage.SCANNER_DIR;

@Service
public class ScannerServiceImpl implements ScannerService {
    @Autowired
    private FileService fileService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public ScanningResult scan(MultipartFile file, boolean includeTransitive) throws Exception {
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
                Namespace space = new Namespace(String.join("/", namespace, folderName), null);
                return packageManagementTool.getDependencies(includeTransitive, space);
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
                    return new ScanningResult();
                }
            }
            try {
                fileService.save(file, ecosystem.getPackageManagementFile(), namespace);
                Namespace space = new Namespace(namespace, null);
                return packageManagementTool.getDependencies(includeTransitive, space);
            } finally {
                fileService.clean(String.join("/", SCANNER_DIR, namespace));
            }
        }
    }
}

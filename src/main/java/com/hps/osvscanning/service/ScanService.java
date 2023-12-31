package com.hps.osvscanning.service;

import com.hps.osvscanning.model.VulnerabilityBulk;
import com.hps.osvscanning.model.Library;
import org.springframework.web.multipart.MultipartFile;

public interface ScanService {
    VulnerabilityBulk retrieve(Library libraryInfo);

    VulnerabilityBulk scan(MultipartFile file) throws Exception;
}

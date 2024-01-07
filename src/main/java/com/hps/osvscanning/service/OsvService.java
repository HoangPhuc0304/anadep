package com.hps.osvscanning.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.osv.VulnerabilityOSVBatchResponse;
import com.hps.osvscanning.model.osv.VulnerabilityOSVResponse;

import java.util.List;

public interface OsvService {
    VulnerabilityOSVResponse findVulnerabilities(Library libraryInfo);

    VulnerabilityOSVBatchResponse findVulnerabilities(List<Library> libraryBulk);
}

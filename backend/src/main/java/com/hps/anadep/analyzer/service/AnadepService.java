package com.hps.anadep.analyzer.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.osv.VulnerabilityOSVBatchResponse;
import com.hps.anadep.model.osv.VulnerabilityOSVResponse;

import java.util.Set;

public interface AnadepService {
    VulnerabilityOSVResponse findVulnerabilities(Library libraryInfo);

    VulnerabilityOSVBatchResponse findVulnerabilities(Set<Library> libraryBulk);
}

package com.hps.anadepscheduler.service;

import com.hps.anadepscheduler.model.osv.*;

public interface AppService {

    VulnerabilityOSVResponse query(LibraryOSVRequest libraryOSVRequest);
    VulnerabilityOSVBatchResponse query(LibraryOSVBatchRequest libraryOSVBatchRequest);
    Vulnerability query(String databasedId);

}

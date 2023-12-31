package com.hps.osvscanning.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.mvn.LibraryBulk;
import com.hps.osvscanning.model.osv.VulnerabilityList;

public interface OsvService {
    VulnerabilityList findVulnerabilities(Library libraryInfo);
}

package com.hps.osvscanning.service;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.VulnerabilityBulk;
import com.hps.osvscanning.model.mvn.LibraryBulk;

public interface MavenService {
    LibraryBulk findLibrary(Library libraryInfo, int rows);
    int getLibraryCount(Library libraryInfo);
}

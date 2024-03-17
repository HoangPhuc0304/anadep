package com.hps.anadep.analyzer.service;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.mvn.LibraryBulk;

public interface MavenService {
    LibraryBulk findLibrary(Library libraryInfo, int rows);
    int getLibraryCount(Library libraryInfo);
}

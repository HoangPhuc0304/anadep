package com.hps.anadep.scanner.util;

import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;

public interface PackageManagementTool {
    ScanningResult getDependencies(boolean includeTransitive, String namespace) throws Exception;
    void createFixFile(SummaryFix summaryFix, String fileName) throws Exception;
}

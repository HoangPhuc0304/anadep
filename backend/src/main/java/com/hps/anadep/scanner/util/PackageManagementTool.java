package com.hps.anadep.scanner.util;

import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.util.Namespace;

public interface PackageManagementTool {
    ScanningResult getDependencies(boolean includeTransitive, Namespace namespace) throws Exception;
    void createFixFile(SummaryFix summaryFix, String fileName) throws Exception;
}

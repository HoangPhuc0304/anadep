package com.hps.osvscanning.scanner.util;

import com.hps.osvscanning.model.Library;

import java.util.Set;

public interface PackageManagementTool {
    Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception;
}

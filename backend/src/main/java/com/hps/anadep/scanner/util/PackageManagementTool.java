package com.hps.anadep.scanner.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.Library;

import java.util.Map;
import java.util.Set;

public interface PackageManagementTool {
    Set<Library> getDependencies(boolean includeTransitive, String namespace) throws Exception;
    boolean isUseLibrary(Map.Entry<Library, String> libraryMap, Library lib) throws JsonProcessingException;
    Map<Library, String> getDependencyMap(String namespace) throws Exception;
}

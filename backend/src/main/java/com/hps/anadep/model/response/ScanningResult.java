package com.hps.anadep.model.response;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.depgraph.Dependency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScanningResult {
    Set<Library> libraries;
    Set<Dependency> dependencies;
    private String projectName;
    private String ecosystem;
    private String path;
    private int libraryCount;
    private boolean includeTransitive;
    private long responseTime;

    public ScanningResult(ScanningResult scanningResult, long responseTime) {
        this.libraries = scanningResult.getLibraries();
        this.dependencies = scanningResult.getDependencies();
        this.projectName = scanningResult.getProjectName();
        this.ecosystem = scanningResult.getEcosystem();
        this.path = scanningResult.getPath();
        this.libraryCount = scanningResult.getLibraryCount();
        this.includeTransitive = scanningResult.isIncludeTransitive();
        this.responseTime = responseTime;
    }
}

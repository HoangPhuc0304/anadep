package com.hps.anadep.model.response;

import com.hps.anadep.model.Library;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanningResult {
    Set<Library> libraries;
    private String ecosystem;
    private int libraryCount;
    private boolean includeTransitive;
    private long responseTime;
}

package com.hps.anadep.model.depgraph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Depgraph {
    private String graphName;
    private List<Artifact> artifacts;
    private List<Dependency> dependencies;
}

package com.hps.anadep.model.depgraph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artifact {
    private String id;
    private String groupId;
    private String artifactId;
    private String version;
}

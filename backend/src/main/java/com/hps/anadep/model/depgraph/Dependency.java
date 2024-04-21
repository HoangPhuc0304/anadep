package com.hps.anadep.model.depgraph;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dependency {
    private String from;
    private String to;
    private int numericFrom;
    private int numericTo;
    private String version;
    private String resolution;
}

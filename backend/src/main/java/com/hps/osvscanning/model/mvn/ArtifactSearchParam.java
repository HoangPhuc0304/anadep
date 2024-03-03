package com.hps.osvscanning.model.mvn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactSearchParam {
    private String q;
    private String core;
    private String indent;
    private String fl;
    private String start;
    private String sort;
    private String rows;
    private String wt;
    private String version;
}

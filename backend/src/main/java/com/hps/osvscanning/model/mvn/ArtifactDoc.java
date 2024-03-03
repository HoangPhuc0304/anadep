package com.hps.osvscanning.model.mvn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactDoc {
    private String id;
    private String g;
    private String a;
    private String v;
    private String p;
    private long timestamp;
    private List<String> ec;
    private List<String> tags;
}

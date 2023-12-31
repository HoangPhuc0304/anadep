package com.hps.osvscanning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Library {
    private String groupId;
    private String artifactId;
    private String version;
}

package com.hps.osvscanning.model.mvn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactSearchResponse {
    private List<ArtifactDoc> docs;
    private int numFound;
    private int start;
}

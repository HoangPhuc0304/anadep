package com.hps.anadep.model.mvn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactSearchHeader {
    private int status;
    @JsonProperty("QTime")
    private int qTime;
    private ArtifactSearchParam params;
}

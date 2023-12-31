package com.hps.osvscanning.model.mvn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtifactSearchHeader {
    private int status;
    @JsonProperty("QTime")
    private int qTime;
    private ArtifactSearchParam params;
}

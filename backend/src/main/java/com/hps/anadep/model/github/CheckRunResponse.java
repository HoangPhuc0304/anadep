package com.hps.anadep.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckRunResponse {
    private String id;
    private String name;
    @JsonProperty("head_sha")
    private String headSha;
    private String status;
    @JsonProperty("started_at")
    private String startedAt;
    private CheckRunOutput output;
}

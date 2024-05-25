package com.hps.anadep.model.github;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckRunRequest {
    private String name;
    @JsonProperty("head_sha")
    private String headSha;
    @JsonProperty("details_url")
    private String detailsUrl;
    private String status;
    @JsonProperty("started_at")
    private String startedAt;
    private String conclusion;
    @JsonProperty("completed_at")
    private String completedAt;
    private CheckRunOutput output;
}

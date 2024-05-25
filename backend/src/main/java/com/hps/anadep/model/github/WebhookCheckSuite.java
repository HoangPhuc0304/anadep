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
public class WebhookCheckSuite {
    private String id;
    @JsonProperty("head_branch")
    private String headBranch;
    @JsonProperty("head_sha")
    private String headSha;
    private String status;
    private String conclusion;
    private String url;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
}

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
public class WebhookHeadCommit {
    private String id;
    @JsonProperty("tree_id")
    private String treeId;
    private String message;
    private String timestamp;
    private String url;
    private Committer committer;
}

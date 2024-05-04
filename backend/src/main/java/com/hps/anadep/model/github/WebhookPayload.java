package com.hps.anadep.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hps.anadep.model.entity.dto.RepoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebhookPayload {
    private String ref;
    @JsonProperty("base_ref")
    private String baseRef;
    private String before;
    private String after;
    private String compare;
    private String created;
    private String deleted;
    private String forced;
    private Committer pusher;
    private Repository repository;
    private Owner sender;
}
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
public class BranchContent {
    private String name;
    private String path;
    private String sha;
    private String size;
    @JsonProperty("download_url")
    private String downloadUrl;
    private String type;
    private String content;
}

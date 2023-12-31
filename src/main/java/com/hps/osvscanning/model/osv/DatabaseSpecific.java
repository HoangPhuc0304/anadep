package com.hps.osvscanning.model.osv;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseSpecific {
    @JsonProperty("github_reviewed_at")
    private String githubReviewedAt;
    @JsonProperty("github_reviewed")
    private boolean githubReviewed;
    private String severity;
    @JsonProperty("cwe_ids")
    private List<String> cweIds;
    @JsonProperty("nvd_published_at")
    private String nvdPublishedAt;
}

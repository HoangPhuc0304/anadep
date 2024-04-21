package com.hps.anadep.model.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchContentRequest {
    private String message;
    private String content;
    private String sha;
    private String branch;
    private Committer committer;
}

package com.hps.anadep.model.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PullRequestRequest {
    private String title;
    private String head;
    private String base;
    private String body;
}

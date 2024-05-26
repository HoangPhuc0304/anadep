package com.hps.anadep.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Repository {
    private Long id;
    private String name;
    @JsonProperty("full_name")
    private String fullName;
    private Owner owner;
    @JsonProperty("private")
    private boolean isPrivate;
    @JsonProperty("html_url")
    private String githubUrl;
    @JsonProperty("default_branch")
    private String defaultBranch;
    private String language;
}

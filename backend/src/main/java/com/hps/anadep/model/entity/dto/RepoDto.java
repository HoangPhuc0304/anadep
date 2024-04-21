package com.hps.anadep.model.entity.dto;

import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RepoDto {

    private UUID id;

    private Long githubRepoId;
    @NotEmpty
    private String name;

    @NotEmpty
    private String fullName;

    private String owner;

    private boolean isPublic;

    private String githubUrl;

    private String defaultBranch;

    private String language;

    private ScanningResult scanningResult = new ScanningResult();

    private UUID userId;

    private AnalysisUIResult vulnerabilityResult = new AnalysisUIResult();
}

package com.hps.anadep.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SecurityAdvisoryResponse {
    @JsonProperty("ghsa_id")
    private String ghsaId;
    private String summary;
    @JsonProperty("cve_id")
    private String cveId;
}

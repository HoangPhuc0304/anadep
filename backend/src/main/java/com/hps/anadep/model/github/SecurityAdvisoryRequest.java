package com.hps.anadep.model.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class SecurityAdvisoryRequest {
    @JsonIgnore
    private String databaseId;
    private String summary;
    private String description;
    @JsonProperty("cve_id")
    private String cveId;
    private List<SecurityAdvisoryVulnerability> vulnerabilities;
    @JsonProperty("cvss_vector_string")
    private String cvssVector;
}

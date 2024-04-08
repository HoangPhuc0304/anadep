package com.hps.anadep.model.response;

import com.hps.anadep.model.Library;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryScan {
    private Library info;
    private List<VulnerabilityResponse> vulns;
}

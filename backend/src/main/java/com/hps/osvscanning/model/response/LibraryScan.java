package com.hps.osvscanning.model.response;

import com.hps.osvscanning.model.Library;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryScan {
    private Library info;
    private List<VulnerabilityResponse> vulns;
}

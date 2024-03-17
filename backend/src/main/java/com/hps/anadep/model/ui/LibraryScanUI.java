package com.hps.anadep.model.ui;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.response.VulnerabilityResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryScanUI {
    private Library info;
    private VulnerabilityResponse vuln;
}

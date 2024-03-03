package com.hps.osvscanning.model.ui;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.response.VulnerabilityResponse;
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

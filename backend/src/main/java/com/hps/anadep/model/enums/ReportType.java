package com.hps.anadep.model.enums;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.ui.LibraryScanUI;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    VULNS(LibraryScanUI.class), SBOM(Library.class);

    private final Class<?> classType;

    public static ReportType getReportType(String rangeTypeStr) {
        for(ReportType rangeType : ReportType.values()) {
            if (rangeType.name().equalsIgnoreCase(rangeTypeStr)) {
                return rangeType;
            }
        }
        throw new RuntimeException(String.format("Cannot find [%s] Report Type", rangeTypeStr));
    }
}

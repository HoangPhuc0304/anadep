package com.hps.anadep.model.enums;

import com.hps.anadep.reporter.util.ExcelTool;
import com.hps.anadep.reporter.util.JsonTool;
import com.hps.anadep.reporter.util.PdfTool;
import com.hps.anadep.reporter.util.ReportTool;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Format {
    PDF(new PdfTool()), EXCEL(new ExcelTool()), JSON(new JsonTool());

    private final ReportTool tool;

    public static Format getFormat(String rangeTypeStr) {
        for(Format rangeType : Format.values()) {
            if (rangeType.name().equalsIgnoreCase(rangeTypeStr)) {
                return rangeType;
            }
        }
        throw new RuntimeException(String.format("Cannot find [%s] Format", rangeTypeStr));
    }
}

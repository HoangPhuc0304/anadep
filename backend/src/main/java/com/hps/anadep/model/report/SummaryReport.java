package com.hps.anadep.model.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SummaryReport {
    private String projectName;
    private String author;
    private String reportType;
    private String exportOn;
    private String ecosystem;
    private int amount;
    private List<?> data;
    private List<?> dependencies;
}

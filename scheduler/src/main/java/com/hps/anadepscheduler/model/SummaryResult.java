package com.hps.anadepscheduler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryResult {
    private int vulnerabilityCount;
    private int dependencyCount;
    private int success;
    private int fail;
    private long executeTime;
}

package com.hps.anadepscheduler.model.osv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Severity {
    private String type;
    private String score;
    private double baseScore;
    private String ranking;
}

package com.hps.anadep.evaluator.enums;

import com.hps.anadep.model.enums.Ecosystem;

public enum Severity {
    NONE("None", 0), LOW("Low", 0.1), MEDIUM("Medium", 4.0), HIGH("High", 7.0), CRITICAL("Critical", 9.0);

    private String name;
    private double minimum;

    Severity(String name, double minimum) {
        this.name = name;
        this.minimum = minimum;
    }

    public String getName() {
        return this.name;
    }

    public double getMinimum() {
        return this.minimum;
    }

    public static Severity getSeverityFromName(String severityStr) {
        for (Severity severity : Severity.values()) {
            if (severity.getName().equalsIgnoreCase(severityStr)) {
                return severity;
            }
        }
        throw new RuntimeException(String.format("Cannot find [%s] ecosystem", severityStr));
    }
}

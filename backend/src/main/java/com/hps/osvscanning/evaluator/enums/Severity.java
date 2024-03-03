package com.hps.osvscanning.evaluator.enums;

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
}

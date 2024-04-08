package com.hps.anadepscheduler.model.osv.enums;

public enum RangeType {
    ECOSYSTEM, SEMVER, GIT;

    public static RangeType getRangeType(String rangeTypeStr) {
        for(RangeType rangeType : RangeType.values()) {
            if (rangeType.name().equalsIgnoreCase(rangeTypeStr)) {
                return rangeType;
            }
        }
        throw new RuntimeException(String.format("Cannot find [%s] RangeType", rangeTypeStr));
    }
}

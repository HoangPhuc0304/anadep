package com.hps.anadep.model.enums;

import com.hps.anadep.exception.NotFoundException;

public enum RangeType {
    ECOSYSTEM, SEMVER, GIT;

    public static RangeType getRangeType(String rangeTypeStr) {
        for(RangeType rangeType : RangeType.values()) {
            if (rangeType.name().equalsIgnoreCase(rangeTypeStr)) {
                return rangeType;
            }
        }
        throw new NotFoundException(String.format("Cannot find [%s] RangeType", rangeTypeStr));
    }
}

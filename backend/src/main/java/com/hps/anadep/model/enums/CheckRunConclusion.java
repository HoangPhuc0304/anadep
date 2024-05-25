package com.hps.anadep.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CheckRunConclusion {
    CANCELLED("cancelled"), FAILURE("failure"), SUCCESS("success"), SKIPPED("skipped");

    private final String name;
}

package com.hps.anadep.model.enums;

import lombok.Getter;

@Getter
public enum Compress {
    ZIP("zip");
    private final String name;

    Compress(String name) {
        this.name = name;
    }
}

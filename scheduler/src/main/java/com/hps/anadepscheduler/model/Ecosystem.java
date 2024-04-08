package com.hps.anadepscheduler.model;

import lombok.Getter;

@Getter
public enum Ecosystem {
    MAVEN("Maven", "https://osv-vulnerabilities.storage.googleapis.com/Maven/all.zip", "storage/maven"),
    NPM("npm", "https://osv-vulnerabilities.storage.googleapis.com/npm/all.zip", "storage/npm"),
    PYPI("PyPI", "https://osv-vulnerabilities.storage.googleapis.com/PyPI/all.zip", "storage/pypi");

    private final String osvName;
    private final String url;
    private final String storage;

    Ecosystem(String osvName, String url, String storage) {
        this.osvName = osvName;
        this.url = url;
        this.storage = storage;
    }

    public static Ecosystem getEcosystem(String ecosystemStr) {
        for (Ecosystem ecosystem : Ecosystem.values()) {
            if (ecosystem.name().equalsIgnoreCase(ecosystemStr)) {
                return ecosystem;
            }
        }
        throw new RuntimeException(String.format("Cannot find [%s] ecosystem", ecosystemStr));
    }
}

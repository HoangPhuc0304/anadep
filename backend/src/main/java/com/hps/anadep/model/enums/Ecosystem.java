package com.hps.anadep.model.enums;

import com.hps.anadep.exception.NotFoundException;
import lombok.Getter;

@Getter
public enum Ecosystem {
    NPM("npm", "npm", "package.json"), MAVEN("Maven", "maven", "pom.xml");
    private final String osvName;
    private final String githubName;
    private final String packageManagementFile;

    Ecosystem(String osvName, String githubName, String packageManagementFile) {
        this.osvName = osvName;
        this.githubName = githubName;
        this.packageManagementFile = packageManagementFile;
    }

    public static Ecosystem getEcosystem(String ecosystemStr) {
        for (Ecosystem ecosystem : Ecosystem.values()) {
            if (ecosystem.name().equalsIgnoreCase(ecosystemStr)) {
                return ecosystem;
            }
        }
        throw new NotFoundException(String.format("Cannot find [%s] ecosystem", ecosystemStr));
    }

    public static Ecosystem getEcosystemFromPackageManagementFile(String filename) {
        for (Ecosystem ecosystem : Ecosystem.values()) {
            if (ecosystem.packageManagementFile.equals(filename)) {
                return ecosystem;
            }
        }
        throw new RuntimeException(String.format("Cannot scan [%s] file", filename));
    }

    public static Ecosystem getEcosystemByOsvName(String osvName) {
        for (Ecosystem ecosystem : Ecosystem.values()) {
            if (ecosystem.osvName.equalsIgnoreCase(osvName)) {
                return ecosystem;
            }
        }
        return null;
    }
}

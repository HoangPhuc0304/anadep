package com.hps.osvscanning.model.enums;

public enum Ecosystem {
    NPM("npm", "package.json"), MAVEN("Maven", "pom.xml");
    private final String osvName;
    private final String packageManagementFile;

    Ecosystem(String osvName, String packageManagementFile) {
        this.osvName = osvName;
        this.packageManagementFile = packageManagementFile;
    }

    public String getPackageManagementFile() {
        return packageManagementFile;
    }

    public String getOsvName() {
        return osvName;
    }

    public static Ecosystem getEcosystem(String ecosystemStr) {
        for (Ecosystem ecosystem : Ecosystem.values()) {
            if (ecosystem.name().equalsIgnoreCase(ecosystemStr)) {
                return ecosystem;
            }
        }
        throw new RuntimeException(String.format("Cannot find [%s] ecosystem", ecosystemStr));
    }

    public static Ecosystem getEcosystemFromPackageManagementFile(String filename) {
        for (Ecosystem ecosystem : Ecosystem.values()) {
            if (ecosystem.packageManagementFile.equals(filename)) {
                return ecosystem;
            }
        }
        throw new RuntimeException(String.format("Cannot scan [%s] file", filename));
    }
}

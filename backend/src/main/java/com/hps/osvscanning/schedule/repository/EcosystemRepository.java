package com.hps.osvscanning.schedule.repository;

public interface EcosystemRepository<T> {
    T findCommonEcosystemByNameAndEcosystemAndTypeAndVersion( String name, String ecosystem, String type, String version);
}

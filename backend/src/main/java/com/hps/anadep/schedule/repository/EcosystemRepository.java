package com.hps.anadep.schedule.repository;

public interface EcosystemRepository<T> {
    T findCommonEcosystemByNameAndEcosystemAndTypeAndVersion( String name, String ecosystem, String type, String version);
}

package com.hps.osvscanning.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document("maven")
public class MavenEcosystem extends CommonEcosystem {
    public MavenEcosystem(String id, String name, String ecosystem, String type, String version, Set<String> vulnId) {
        super(id, name, ecosystem, type, version, vulnId);
    }

    public MavenEcosystem(String name, String ecosystem, String type, String version, Set<String> vulnId) {
        super(name, ecosystem, type, version, vulnId);
    }

    public MavenEcosystem() {
        super();
    }
}

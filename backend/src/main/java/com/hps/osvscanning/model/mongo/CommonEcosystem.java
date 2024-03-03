package com.hps.osvscanning.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class CommonEcosystem {
    @Id
    private String id;
    private String name;
    private String ecosystem;
    private String type;
    private String version;
    private Set<String> vulnId;

    public CommonEcosystem(String name, String ecosystem, String type, String version, Set<String> vulnId) {
        this.name = name;
        this.ecosystem = ecosystem;
        this.type = type;
        this.version = version;
        this.vulnId = vulnId;
    }
}

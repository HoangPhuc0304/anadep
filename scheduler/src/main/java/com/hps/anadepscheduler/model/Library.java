package com.hps.anadepscheduler.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Library {
    private String name;
    private String version;
    private String ecosystem;

    public Library(Library library) {
        this.name = library.name;
        this.version = library.version;
        this.ecosystem = library.ecosystem;
    }

    @JsonSetter("ecosystem")
    public void setEcosystem(String ecosystem) {
        this.ecosystem = Ecosystem.getEcosystem(ecosystem).getOsvName();
    }
}

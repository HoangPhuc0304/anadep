package com.hps.anadep.model.ui;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hps.anadep.model.enums.Ecosystem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryUI {
    private String name;
    private String version;
    private String ecosystem;

    public LibraryUI(LibraryUI library) {
        this.name = library.name;
        this.version = library.version;
        this.ecosystem = library.ecosystem;
    }

    @JsonSetter("ecosystem")
    public void setEcosystem(String ecosystem) {
        this.ecosystem = Ecosystem.getEcosystem(ecosystem).getOsvName();
    }
}

package com.hps.osvscanning.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hps.osvscanning.model.enums.Ecosystem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

//    @JsonIgnore
//    public String getLibraryShortName() {
//        switch (Ecosystem.getEcosystem(this.ecosystem)) {
//            case MAVEN -> {
//                return groupId + ":" + artifactId;
//            }
//            case NPM -> {
//                return artifactId;
//            }
//        }
//        return null;
//    }
}

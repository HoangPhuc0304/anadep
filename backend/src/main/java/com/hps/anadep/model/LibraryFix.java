package com.hps.anadep.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hps.anadep.model.enums.Ecosystem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LibraryFix {
    private String name;
    private String currentVersion;
    private String ecosystem;
    private String fixedVersion;

    @JsonSetter("ecosystem")
    public void setEcosystem(String ecosystem) {
        this.ecosystem = Ecosystem.getEcosystem(ecosystem).getOsvName();
    }

    public LibraryFix(Library library, String fixedVersion) {
        this.name = library.getName();
        this.currentVersion = library.getVersion();
        this.ecosystem = library.getEcosystem();
        this.fixedVersion = fixedVersion;
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

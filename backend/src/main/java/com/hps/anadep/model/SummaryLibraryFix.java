package com.hps.anadep.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.hps.anadep.model.enums.Ecosystem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class SummaryLibraryFix {
    private String name;
    private String currentVersion;
    private String ecosystem;
    private String fixedVersion;
    private Set<Library> usedBy;
    private String severity;
    private String description;

    @JsonSetter("ecosystem")
    public void setEcosystem(String ecosystem) {
        this.ecosystem = Ecosystem.getEcosystem(ecosystem).getOsvName();
    }

    public SummaryLibraryFix(LibraryFix library, Set<Library> usedBy, String description) {
        this.name = library.getName();
        this.currentVersion = library.getCurrentVersion();
        this.ecosystem = library.getEcosystem();
        this.fixedVersion = library.getFixedVersion();
        this.severity = library.getSeverity();
        this.usedBy = usedBy;
        this.description = description;
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

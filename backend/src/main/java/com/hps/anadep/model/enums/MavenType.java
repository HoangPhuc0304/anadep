package com.hps.anadep.model.enums;

import com.hps.anadep.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MavenType {
    JAVA_SOURCE("java-source"), JAR("jar"), WAR("war");

    private final String name;

    public static MavenType getMavenType(String name) {
        for (MavenType mavenType : MavenType.values()) {
            if (mavenType.name().equalsIgnoreCase(name)) {
                return mavenType;
            }
        }
        throw new NotFoundException(String.format("Cannot find [%s] Maven Type", name));
    }
}

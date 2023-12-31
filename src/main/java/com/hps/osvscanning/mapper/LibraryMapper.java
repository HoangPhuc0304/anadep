package com.hps.osvscanning.mapper;

import com.hps.osvscanning.model.Library;
import com.hps.osvscanning.model.LibraryVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LibraryMapper {
    private static final String MAVEN = "Maven";

    @Mapping(target = "libraryEcosystem.name", expression = "java(getLibraryName(library))")
    @Mapping(target = "libraryEcosystem.ecosystem", constant = MAVEN)
    @Mapping(target = "commit", ignore = true)
    @Mapping(target = "nextPageToken", ignore = true)
    @Mapping(target = "libraryEcosystem.purl", ignore = true)
    public abstract LibraryVersion libraryToVersion(Library library);

    String getLibraryName(Library libraryInfo) {
        return libraryInfo.getGroupId() + ":" + libraryInfo.getArtifactId();
    }
}

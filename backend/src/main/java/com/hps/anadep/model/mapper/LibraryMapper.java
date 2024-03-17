package com.hps.anadep.model.mapper;

import com.hps.anadep.model.Library;
import com.hps.anadep.model.enums.Ecosystem;
import com.hps.anadep.model.osv.LibraryOSVRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class LibraryMapper {
    @Mapping(target = "libraryEcosystem.name", source = "name")
    @Mapping(target = "libraryEcosystem.ecosystem", expression = "java(getEcosystem(library))")
    @Mapping(target = "commit", ignore = true)
    @Mapping(target = "nextPageToken", ignore = true)
    @Mapping(target = "libraryEcosystem.purl", ignore = true)
    public abstract LibraryOSVRequest libraryToOSVRequest(Library library);

    String getEcosystem(Library library) {
        return Ecosystem.getEcosystem(library.getEcosystem()).getOsvName();
    }
}

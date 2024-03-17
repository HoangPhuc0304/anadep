package com.hps.anadep.model.osv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hps.anadep.model.LibraryEcosystem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryOSVRequest {
    private String commit;
    private String version;
    @JsonProperty("package")
    private LibraryEcosystem libraryEcosystem;
    private String nextPageToken;
}

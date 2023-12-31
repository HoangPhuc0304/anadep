package com.hps.osvscanning.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryVersion {
    private String commit;
    private String version;
    @JsonProperty("package")
    private LibraryEcosystem libraryEcosystem;
    private String nextPageToken;
}

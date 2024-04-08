package com.hps.anadepscheduler.model.osv;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

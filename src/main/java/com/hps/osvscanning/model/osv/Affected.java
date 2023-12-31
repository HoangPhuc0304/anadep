package com.hps.osvscanning.model.osv;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hps.osvscanning.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Affected {
    @JsonProperty("package")
    private LibraryEcosystem libraryEcosystem;
    private List<Range> ranges;
    private List<String> versions;
    @JsonProperty("database_specific")
    private DatabaseSourceLink databaseSpecific;
}

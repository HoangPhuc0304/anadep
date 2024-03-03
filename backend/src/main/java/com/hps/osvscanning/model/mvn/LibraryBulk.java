package com.hps.osvscanning.model.mvn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryBulk {
    private ArtifactSearchHeader responseHeader;
    private ArtifactSearchResponse response;
}

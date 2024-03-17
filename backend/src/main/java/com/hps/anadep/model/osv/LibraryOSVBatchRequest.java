package com.hps.anadep.model.osv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryOSVBatchRequest {
    List<LibraryOSVRequest> queries;
}

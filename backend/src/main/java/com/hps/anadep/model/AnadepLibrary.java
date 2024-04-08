package com.hps.anadep.model;

import com.hps.anadep.model.osv.Vulnerability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnadepLibrary {
    private Library info;
    private List<Vulnerability> vulns;
}

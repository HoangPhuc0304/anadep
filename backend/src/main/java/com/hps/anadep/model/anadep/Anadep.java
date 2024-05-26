package com.hps.anadep.model.anadep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Anadep {
    private AnadepScanner scanner;
    private AnadepAnalyzer analyzer;
}

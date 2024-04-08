package com.hps.anadepscheduler.model.osv;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEcosystem {
    @NotBlank
    private String name;
    @NotBlank
    private String ecosystem;
    private String purl;
}

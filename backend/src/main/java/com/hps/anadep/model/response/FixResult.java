package com.hps.anadep.model.response;

import com.hps.anadep.model.LibraryFix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FixResult {
    private List<LibraryFix> libs;
    private String ecosystem;
    private long responseTime;
}

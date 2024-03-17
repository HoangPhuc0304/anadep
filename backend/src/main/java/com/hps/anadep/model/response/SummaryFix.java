package com.hps.anadep.model.response;

import com.hps.anadep.model.SummaryLibraryFix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryFix {
    private List<SummaryLibraryFix> libs;
    private String ecosystem;
    private long responseTime;
}

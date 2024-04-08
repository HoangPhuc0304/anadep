package com.hps.anadepscheduler.model;

import com.hps.anadepscheduler.model.osv.Vulnerability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryScan {
    private Library info;
    private List<Vulnerability> vulns;
}

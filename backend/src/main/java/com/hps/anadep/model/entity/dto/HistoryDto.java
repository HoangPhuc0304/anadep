package com.hps.anadep.model.entity.dto;

import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryDto {

    private UUID id;

    private ScanningResult scanningResult = new ScanningResult();

    private AnalysisUIResult vulnerabilityResult = new AnalysisUIResult();

    private Date createdAt;

    private String type;

    private String path;

    private UUID repoId;

    private UUID userId;
}

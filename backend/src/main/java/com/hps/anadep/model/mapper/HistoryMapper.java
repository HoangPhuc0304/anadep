package com.hps.anadep.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.entity.History;
import com.hps.anadep.model.entity.dto.HistoryDto;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public abstract class HistoryMapper {

    @Mapping(target = "scanningResult", expression = "java(getScanningResult(history))")
    @Mapping(target = "repoId", source = "history.repo.id")
    @Mapping(target = "vulnerabilityResult", expression = "java(getVulnerabilityResult(history))")
    public abstract HistoryDto mapToDto(History history);

    ScanningResult getScanningResult(History history) {
        if (StringUtils.hasText(history.getScanningResult())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(history.getScanningResult(), ScanningResult.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new ScanningResult();
    }

    AnalysisUIResult getVulnerabilityResult(History history) {
        if (StringUtils.hasText(history.getVulnerabilityResult())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(history.getVulnerabilityResult(), AnalysisUIResult.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new AnalysisUIResult();
    }
}

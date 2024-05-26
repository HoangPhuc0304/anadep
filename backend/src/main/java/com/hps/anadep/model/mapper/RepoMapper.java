package com.hps.anadep.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.entity.History;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.repository.HistoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class RepoMapper {
    @Autowired
    private HistoryRepository historyRepository;

    @Mapping(target = "scanningResult", expression = "java(getScanningResult(repo))")
    @Mapping(target = "userIds", expression = "java(getUserIds(repo))")
    @Mapping(target = "vulnerabilityResult", expression = "java(getVulnerabilityResult(repo))")
    public abstract RepoDto mapToDto(Repo repo);

    ScanningResult getScanningResult(Repo repo) {
        List<History> histories = historyRepository.findAllOrderByCreatedAtDesc(repo.getId(), ReportType.SBOM.name());
        if (!CollectionUtils.isEmpty(histories) && StringUtils.hasText(histories.get(0).getScanningResult())) {
            String scanningResultStr = histories.get(0).getScanningResult();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(scanningResultStr, ScanningResult.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new ScanningResult();
    }

    AnalysisUIResult getVulnerabilityResult(Repo repo) {
        List<History> histories = historyRepository.findAllOrderByCreatedAtDesc(repo.getId(), ReportType.VULNS.name());
        if (!CollectionUtils.isEmpty(histories) && StringUtils.hasText(histories.get(0).getVulnerabilityResult())) {
            String vulnerabilityResultStr = histories.get(0).getVulnerabilityResult();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(vulnerabilityResultStr, AnalysisUIResult.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new AnalysisUIResult();
    }

    Set<UUID> getUserIds(Repo repo) {
        return repo.getUsers().stream().map(User::getId).collect(Collectors.toSet());
    }
}

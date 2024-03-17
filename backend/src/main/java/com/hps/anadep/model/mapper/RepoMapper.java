package com.hps.anadep.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.response.ScanningResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public abstract class RepoMapper {
    @Mapping(target = "scanningResult", expression = "java(getScanningResult(repo))")
    @Mapping(target = "userId", source = "repo.user.id")
    @Mapping(target = "summaryVulnerability", ignore = true)
    public abstract RepoDto mapToDto(Repo repo);

    ScanningResult getScanningResult(Repo repo) {
        if (StringUtils.hasText(repo.getScanningResult())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(repo.getScanningResult(), ScanningResult.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return new ScanningResult();
    }
}

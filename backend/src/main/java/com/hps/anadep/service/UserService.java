package com.hps.anadep.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.entity.dto.SummaryVulnerabilityDto;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.security.AppUser;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User save(User user);
    RepoDto save(RepoDto repoDto, AppUser appUser);
    SummaryVulnerabilityDto save(SummaryVulnerabilityDto summaryVulnerabilityDto, AppUser appUser);
    void update(AuthTokenDto authTokenDto);
    void update(String repoId, ScanningResult scanningResult, AppUser appUser) throws JsonProcessingException;
    void delete(String repoId, AppUser appUser);

    List<RepoDto> findAll(AppUser appUser);

    RepoDto findRepoById(String repoId, AppUser appUser);

    User findById(UUID idr);
}

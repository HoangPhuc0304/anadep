package com.hps.anadep.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.entity.dto.HistoryDto;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.security.AppUser;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User save(User user);
    RepoDto save(RepoDto repoDto, AppUser appUser);
    void update(AuthTokenDto authTokenDto);
    void update(String repoId, ScanningResult scanningResult, AppUser appUser) throws JsonProcessingException;
    void update(String repoId, AnalysisUIResult analysisUIResult, AppUser appUser) throws JsonProcessingException;
    void delete(String repoId, AppUser appUser);

    List<RepoDto> findAll(AppUser appUser);

    RepoDto findRepoById(String repoId, AppUser appUser);

    User findById(UUID idr);

    List<HistoryDto> findAllHistories(String repoId, String type, AppUser appUser);

    HistoryDto findHistoryById(String repoId, String historyId, AppUser appUser);

    void deleteHistoryById(String repoId, String historyId, AppUser appUser);
}

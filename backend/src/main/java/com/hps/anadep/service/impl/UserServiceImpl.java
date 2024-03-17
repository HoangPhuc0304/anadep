package com.hps.anadep.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.SummaryVulnerability;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.entity.dto.SummaryVulnerabilityDto;
import com.hps.anadep.model.mapper.RepoMapper;
import com.hps.anadep.model.mapper.SummaryVulnerabilityMapper;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.repository.AuthTokenRepository;
import com.hps.anadep.repository.RepoRepository;
import com.hps.anadep.repository.SummaryVulnerabilityRepository;
import com.hps.anadep.repository.UserRepository;
import com.hps.anadep.security.AppUser;
import com.hps.anadep.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private SummaryVulnerabilityRepository summaryVulnerabilityRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private SummaryVulnerabilityMapper summaryVulnerabilityMapper;

    @Autowired
    private RepoMapper repoMapper;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public RepoDto save(RepoDto repoDto, AppUser appUser) {
        validateUserId(repoDto.getUserId(), appUser);

        Repo repo = new Repo();
        BeanUtils.copyProperties(repoDto, repo, "dependencies", "user");
        UUID userId = repoDto.getUserId();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", userId)));
        repo.setUser(user);
        return repoMapper.mapToDto(repoRepository.save(repo));
    }

    @Override
    public SummaryVulnerabilityDto save(SummaryVulnerabilityDto summaryVulnerabilityDto, AppUser appUser) {
        UUID repoId = summaryVulnerabilityDto.getRepoId();
        Repo repo = repoRepository.findById(repoId).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);

        SummaryVulnerability summaryVulnerability = new SummaryVulnerability();
        BeanUtils.copyProperties(summaryVulnerabilityDto, summaryVulnerability, "repository");

        summaryVulnerability.setRepository(repo);
        return summaryVulnerabilityMapper.mapToDto(summaryVulnerabilityRepository.save(summaryVulnerability));
    }

    @Override
    public void update(AuthTokenDto authTokenDto) {
        User user = userRepository.findById(authTokenDto.getUserId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", authTokenDto.getUserId())));
        AuthToken authToken = authTokenRepository.findByUser(user).orElse(null);
        if (authToken == null) {
            authToken = new AuthToken();
            authToken.setUser(user);
        }
        authToken.setGithubToken(authTokenDto.getGithubToken());
        authTokenRepository.save(authToken);
    }

    @Override
    public void update(String repoId, ScanningResult scanningResult, AppUser appUser) throws JsonProcessingException {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);

        ObjectMapper objectMapper = new ObjectMapper();
        repo.setScanningResult(objectMapper.writeValueAsString(scanningResult));
        repoRepository.save(repo);
    }

    @Override
    public void delete(String repoId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);

        repoRepository.deleteById(UUID.fromString(repoId));
    }

    @Override
    public List<RepoDto> findAll(AppUser appUser) {
        User user = userRepository.findById(appUser.getId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
        return repoRepository.findAllByUser(user).stream().map(
                repo -> repoMapper.mapToDto(repo)).collect(Collectors.toList());
    }

    @Override
    public RepoDto findRepoById(String repoId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUser().getId(), appUser);
        RepoDto repoDto = repoMapper.mapToDto(repo);
        repoDto.setSummaryVulnerability(summaryVulnerabilityMapper.mapToDto(repo.getSummaryVulnerability()));
        return repoDto;
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", id)));
    }

    private void validateUserId(UUID id, AppUser appUser) {
        if (!appUser.getId().equals(id)) {
            throw new RuntimeException("Authorize failed");
        }
    }
}

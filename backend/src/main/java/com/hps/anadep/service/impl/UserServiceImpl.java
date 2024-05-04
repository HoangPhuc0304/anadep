package com.hps.anadep.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hps.anadep.model.entity.AuthToken;
import com.hps.anadep.model.entity.History;
import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.entity.dto.HistoryDto;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.enums.ReportType;
import com.hps.anadep.model.mapper.HistoryMapper;
import com.hps.anadep.model.mapper.RepoMapper;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.repository.AuthTokenRepository;
import com.hps.anadep.repository.HistoryRepository;
import com.hps.anadep.repository.RepoRepository;
import com.hps.anadep.repository.UserRepository;
import com.hps.anadep.security.AppUser;
import com.hps.anadep.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepoRepository repoRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private RepoMapper repoMapper;

    @Autowired
    private HistoryMapper historyMapper;

    @Override
    public User save(User user) {
        User savedUser = userRepository.findByGithubUserId(user.getGithubUserId()).orElse(user);
        return userRepository.save(savedUser);
    }

    @Override
    @Transactional
    public RepoDto save(RepoDto repoDto, AppUser appUser) {
        //UserIds should be set of 1 element
        validateUserId(repoDto.getUserIds(), appUser);
        UUID userId = repoDto.getUserIds().stream().findFirst().get();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", userId)));

        Repo repo = repoRepository.findByGithubRepoId(repoDto.getGithubRepoId()).orElse(null);
        if (repo == null) {
            repo = new Repo();
            BeanUtils.copyProperties(repoDto, repo, "dependencies", "user");
        }

        Set<User> users = repo.getUsers() == null ? new HashSet<>() : repo.getUsers();
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        repo.setUsers(users);
        return repoMapper.mapToDto(repoRepository.save(repo));
    }

    @Override
    public void update(String repoId, ScanningResult scanningResult, AppUser appUser) throws JsonProcessingException {
        User user = userRepository.findById(appUser.getId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);

        ObjectMapper objectMapper = new ObjectMapper();
        History history = historyRepository.save(
                History.builder()
                        .scanningResult(objectMapper.writeValueAsString(scanningResult))
                        .type(ReportType.SBOM.name())
                        .createdAt(new Date())
                        .repo(repo)
                        .user(user).build());
        historyRepository.save(history);
    }

    @Override
    public void update(String repoId, AnalysisUIResult analysisUIResult, AppUser appUser) throws JsonProcessingException {
        User user = userRepository.findById(appUser.getId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);

        ObjectMapper objectMapper = new ObjectMapper();
        History history = historyRepository.save(
                History.builder()
                        .vulnerabilityResult(objectMapper.writeValueAsString(analysisUIResult))
                        .type(ReportType.VULNS.name())
                        .createdAt(new Date())
                        .repo(repo)
                        .user(user).build());
        historyRepository.save(history);
    }

    @Override
    @Transactional
    public void delete(String repoId, AppUser appUser) {
        User user = userRepository.findById(appUser.getId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);

        historyRepository.deleteAllByRepoAndUser(repo, user);
        Set<User> users = repo.getUsers();
        users.removeIf(u -> u.getId().equals(appUser.getId()));
        repo.setUsers(users);
        repoRepository.save(repo);
    }

    @Override
    public List<RepoDto> findAll(AppUser appUser) {
        User user = userRepository.findById(appUser.getId()).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", appUser.getId())));
        return user.getRepos().stream().map(
                repo -> repoMapper.mapToDto(repo)).collect(Collectors.toList());
    }

    @Override
    public RepoDto findRepoById(String repoId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);
        return repoMapper.mapToDto(repo);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("The user with id [%s] doesn't exist", id)));
    }

    @Override
    public List<HistoryDto> findAllHistories(String repoId, String type, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);
        return historyRepository.findAllOrderByCreatedAtDesc(UUID.fromString(repoId), type).stream().map(
                history -> historyMapper.mapToDto(history)
        ).collect(Collectors.toList());
    }

    @Override
    public HistoryDto findHistoryById(String repoId, String historyId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);
        History history = historyRepository.findByIdAndRepo(UUID.fromString(historyId), repo).orElseThrow(
                () -> new RuntimeException(String.format("The history with id [%s] doesn't exist", historyId)));
        return historyMapper.mapToDto(history);
    }

    @Override
    @Transactional
    public void deleteHistoryById(String repoId, String historyId, AppUser appUser) {
        Repo repo = repoRepository.findById(UUID.fromString(repoId)).orElseThrow(
                () -> new RuntimeException(String.format("The repo with id [%s] doesn't exist", repoId)));
        validateUserId(repo.getUsers().stream().map(User::getId).collect(Collectors.toSet()), appUser);
        History history = historyRepository.findByIdAndRepo(UUID.fromString(historyId), repo).orElseThrow(
                () -> new RuntimeException(String.format("The history with id [%s] doesn't exist", historyId)));
        historyRepository.delete(history);
    }

    private void validateUserId(Set<UUID> ids, AppUser appUser) {
        if (CollectionUtils.isEmpty(ids) || !ids.contains(appUser.getId())) {
            throw new RuntimeException("Authorize failed");
        }
    }
}

package com.hps.anadep.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.entity.dto.HistoryDto;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.github.AccessTokenRequest;
import com.hps.anadep.model.github.AccessTokenResponse;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.ui.AnalysisUIResult;
import com.hps.anadep.security.AppUser;
import com.hps.anadep.service.GitHubService;
import com.hps.anadep.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private GitHubService gitHubService;

    @PostMapping("/api/token")
    @ResponseStatus(HttpStatus.OK)
    public AccessTokenResponse getToken(@Valid @RequestBody AccessTokenRequest accessTokenRequest) {
        return gitHubService.getToken(accessTokenRequest);
    }

    @PostMapping("/api/user")
    @ResponseStatus(HttpStatus.OK)
    public User save(@Valid @RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/api/user/token")
    @ResponseStatus(HttpStatus.OK)
    public void update(@Valid @RequestBody AuthTokenDto authTokenDto) {
        userService.update(authTokenDto);
    }

    @GetMapping("/api/user")
    @ResponseStatus(HttpStatus.OK)
    public User get(@AuthenticationPrincipal AppUser appUser) {
        return userService.findById(appUser.getId());
    }

    @PostMapping("/api/repo")
    @ResponseStatus(HttpStatus.OK)
    public RepoDto save(@Valid @RequestBody RepoDto repoDto, @AuthenticationPrincipal AppUser appUser) {
        return userService.save(repoDto, appUser);
    }

    @GetMapping("/api/repo")
    @ResponseStatus(HttpStatus.OK)
    public List<RepoDto> getAll(@AuthenticationPrincipal AppUser appUser) {
        return userService.findAll(appUser);
    }

    @PutMapping("/api/repo/{repoId}/scan")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("repoId") String repoId, @RequestBody ScanningResult scanningResult,
                       @AuthenticationPrincipal AppUser appUser) throws JsonProcessingException {
        userService.update(repoId, scanningResult, appUser);
    }

    @PutMapping("/api/repo/{repoId}/analyze")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("repoId") String repoId, @RequestBody AnalysisUIResult analysisUIResult,
                       @AuthenticationPrincipal AppUser appUser) throws JsonProcessingException {
        userService.update(repoId, analysisUIResult, appUser);
    }

    @GetMapping("/api/repo/{repoId}")
    @ResponseStatus(HttpStatus.OK)
    public RepoDto getById(@PathVariable("repoId") String repoId, @AuthenticationPrincipal AppUser appUser) {
        return userService.findRepoById(repoId, appUser);
    }

    @DeleteMapping("/api/repo/{repoId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("repoId") String repoId, @AuthenticationPrincipal AppUser appUser) {
        userService.delete(repoId, appUser);
    }

    @GetMapping("/api/repo/{repoId}/history")
    @ResponseStatus(HttpStatus.OK)
    public List<HistoryDto> getAll(@PathVariable("repoId") String repoId, @RequestParam(value = "type", required = false) String type, @AuthenticationPrincipal AppUser appUser) {
        return userService.findAllHistories(repoId, type, appUser);
    }

    @GetMapping("/api/repo/{repoId}/history/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public HistoryDto getById(@PathVariable("repoId") String repoId, @PathVariable("historyId") String historyId, @AuthenticationPrincipal AppUser appUser) {
        return userService.findHistoryById(repoId, historyId, appUser);
    }

    @DeleteMapping("/api/repo/{repoId}/history/{historyId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable("repoId") String repoId, @PathVariable("historyId") String historyId, @AuthenticationPrincipal AppUser appUser) {
        userService.deleteHistoryById(repoId, historyId, appUser);
    }
}

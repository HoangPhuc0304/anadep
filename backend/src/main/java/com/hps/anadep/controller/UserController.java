package com.hps.anadep.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hps.anadep.model.entity.User;
import com.hps.anadep.model.entity.dto.AuthTokenDto;
import com.hps.anadep.model.entity.dto.RepoDto;
import com.hps.anadep.model.entity.dto.SummaryVulnerabilityDto;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.security.AppUser;
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

    @PutMapping("/api/repo/{repoId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("repoId") String repoId, @RequestBody ScanningResult scanningResult,
                       @AuthenticationPrincipal AppUser appUser) throws JsonProcessingException {
        userService.update(repoId, scanningResult, appUser);
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

    @PostMapping("/api/summary")
    @ResponseStatus(HttpStatus.OK)
    public SummaryVulnerabilityDto save(@Valid @RequestBody SummaryVulnerabilityDto summaryVulnerabilityDto,
                                        @AuthenticationPrincipal AppUser appUser) {
        return userService.save(summaryVulnerabilityDto, appUser);
    }
}

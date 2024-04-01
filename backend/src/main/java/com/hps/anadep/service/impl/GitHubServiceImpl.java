package com.hps.anadep.service.impl;

import com.hps.anadep.analyzer.client.GithubClient;
import com.hps.anadep.model.github.AccessTokenRequest;
import com.hps.anadep.model.github.AccessTokenResponse;
import com.hps.anadep.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubServiceImpl implements GitHubService {
    @Autowired
    private GithubClient githubClient;

    @Override
    public AccessTokenResponse getToken(AccessTokenRequest accessTokenRequest) {
        String response = githubClient.getAccessToken(accessTokenRequest.getCode());
        if (!response.contains("access_token=")) {
            throw new RuntimeException("Authorize failed");
        }

        String accessToken = response.split("&")[0].split("=")[1];
        String scope = response.split("&")[1].split("=")[1];
        String tokenType = response.split("&")[2].split("=")[1];
        return AccessTokenResponse.builder()
                .accessToken(accessToken)
                .scope(scope)
                .tokenType(tokenType)
                .build();
    }
}

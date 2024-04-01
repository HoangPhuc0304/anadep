package com.hps.anadep.service;

import com.hps.anadep.model.github.AccessTokenRequest;
import com.hps.anadep.model.github.AccessTokenResponse;

public interface GitHubService {
    AccessTokenResponse getToken(AccessTokenRequest accessTokenRequest);
}

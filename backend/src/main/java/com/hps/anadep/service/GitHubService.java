package com.hps.anadep.service;

import com.hps.anadep.model.entity.Repo;
import com.hps.anadep.model.enums.GitHubAction;
import com.hps.anadep.model.github.AccessTokenRequest;
import com.hps.anadep.model.github.AccessTokenResponse;
import com.hps.anadep.model.github.RefreshTokenRequest;
import com.hps.anadep.model.github.WebhookPayload;
import com.hps.anadep.model.response.ScanningResult;
import com.hps.anadep.model.response.SummaryFix;
import com.hps.anadep.model.ui.AnalysisUIResult;

import java.io.IOException;

public interface GitHubService {
    AccessTokenResponse getToken(AccessTokenRequest accessTokenRequest);

    AccessTokenResponse getRefreshToken(RefreshTokenRequest refreshTokenRequest);

    void createFixPullRequest(Repo repo, AnalysisUIResult analysisUIResult, ScanningResult scanningResult, SummaryFix summaryFix, String accessToken) throws IOException;

    void handleDelivery(WebhookPayload webhookPayload, GitHubAction action);
}

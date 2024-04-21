package com.hps.anadep.analyzer.client;

import com.hps.anadep.model.github.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class GithubClient {
    @Value("${url.githubClient}")
    private String url;

    @Value("${url.authGithubClient}")
    private String authUrl;

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    private static final String GET_BRANCH_URL_FORMAT = "/repos/%s/git/refs/heads/%s";
    private static final String POST_BRANCH_URL_FORMAT = "/repos/%s/git/refs";
    private static final String GET_BRANCH_CONTENT_URL_FORMAT = "/repos/%s/contents/%s";
    private static final String PUT_BRANCH_CONTENT_URL_FORMAT = "/repos/%s/contents/%s";
    private static final String PULL_REQUEST_URL_FORMAT = "/repos/%s/pulls";
    private static final String SECURITY_ADVISORY_URL_FORMAT = "/repos/%s/security-advisories";
    private static final String SECURITY_ADVISORY_URL_UPDATE_FORMAT = "/repos/%s/security-advisories/%s";
    private static final String BEARER_TOKEN_FORMAT = "Bearer %s";
    private static final String FIX_MESSAGE = "Fix vulnerabilities";
    private static final String NAME_BOT = "Anadep Bot";
    private static final String EMAIL_BOT = "anadepbot@gmail.com";
    private static final String FIX_PULL_REQUEST_TITLE = "Fix vulnerabilities on %s";

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private WebClient.Builder webClientBuilder;

    public byte[] download(String repo, String accessToken) {
        RequestCallback requestCallback = null;

        if (StringUtils.hasText(accessToken)) {
            requestCallback = request -> {
                request.getHeaders()
                        .setBearerAuth(accessToken);
            };
        }

        return restTemplate.execute(
                String.format("%s/repos/%s/zipball", url, repo),
                HttpMethod.GET,
                requestCallback,
                clientHttpResponse -> clientHttpResponse.getBody().readAllBytes()
        );
    }

    public String getAccessToken(String code) {
        return webClientBuilder.build().post()
                .uri(authUrl, builder -> builder.path("/login/oauth/access_token")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("code", code)
                        .build()
                )
                .retrieve().bodyToMono(String.class).block();
    }

    public Branch getBranch(String fullName, String defaultBranch, String accessToken) {
        return webClientBuilder.build().get()
                .uri(url, builder -> builder.path(GET_BRANCH_URL_FORMAT.formatted(fullName, defaultBranch)).build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .retrieve().bodyToMono(Branch.class).block();
    }

    public Branch createBranch(String fullName, BranchRequest branchRequest, String accessToken) {
        return webClientBuilder.build().post()
                .uri(url, builder -> builder.path(POST_BRANCH_URL_FORMAT.formatted(fullName)).build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .bodyValue(branchRequest)
                .retrieve().bodyToMono(Branch.class).block();
    }

    public BranchContent getBranchContent(String fullName, String branchName, String manifestFile, String accessToken) {
        return webClientBuilder.build().get()
                .uri(url, builder -> builder.path(GET_BRANCH_CONTENT_URL_FORMAT.formatted(fullName, manifestFile))
                        .queryParam("ref", branchName)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .retrieve().bodyToMono(BranchContent.class).block();
    }

    public void updateBranch(String fullName, String branchName, String manifestFile, BranchContent branchContent, String encodedString, String accessToken) {
        BranchContentRequest request = BranchContentRequest.builder()
                .message(FIX_MESSAGE)
                .content(encodedString)
                .sha(branchContent.getSha())
                .branch(branchName)
                .committer(new Committer(NAME_BOT, EMAIL_BOT))
                .build();

        webClientBuilder.build().put()
                .uri(url, builder -> builder.path(PUT_BRANCH_CONTENT_URL_FORMAT.formatted(fullName, manifestFile)).build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .bodyValue(request)
                .retrieve().bodyToMono(Void.class).block();
    }

    public void createPullRequest(String fullName, String branchName, String baseName, String content, String accessToken) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        PullRequestRequest request = PullRequestRequest.builder()
                .title(FIX_PULL_REQUEST_TITLE.formatted(currentDateTime))
                .head(branchName)
                .base(baseName)
                .body(content)
                .build();

        webClientBuilder.build().post()
                .uri(url, builder -> builder.path(PULL_REQUEST_URL_FORMAT.formatted(fullName)).build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .bodyValue(request)
                .retrieve().bodyToMono(Void.class).block();
    }

    public void createSecurityAdvisory(String fullName, SecurityAdvisoryRequest request, String accessToken) {
        webClientBuilder.build().post()
                .uri(url, builder -> builder.path(SECURITY_ADVISORY_URL_FORMAT.formatted(fullName)).build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .bodyValue(request)
                .retrieve().bodyToMono(Void.class).block();
    }

    public void updateSecurityAdvisory(String fullName, String ghsaId, SecurityAdvisoryRequest request, String accessToken) {
        webClientBuilder.build().patch()
                .uri(url, builder -> builder.path(SECURITY_ADVISORY_URL_UPDATE_FORMAT.formatted(fullName, ghsaId)).build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .bodyValue(request)
                .retrieve().bodyToMono(Void.class).block();
    }

    public SecurityAdvisoryResponse[] getSecurityAdvisories(String fullName, String accessToken) {
        return webClientBuilder.build().get()
                .uri(url, builder -> builder.path(SECURITY_ADVISORY_URL_FORMAT.formatted(fullName))
                        .queryParam("state", "draft")
                        .build())
                .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN_FORMAT.formatted(accessToken))
                .retrieve().bodyToMono(SecurityAdvisoryResponse[].class).block();
    }
}

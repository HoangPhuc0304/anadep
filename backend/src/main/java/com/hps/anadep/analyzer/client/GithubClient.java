package com.hps.anadep.analyzer.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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
}

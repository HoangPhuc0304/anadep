package com.hps.anadep.analyzer.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {
    @Value("${url.githubClient}")
    private String url;

    private RestTemplate restTemplate = new RestTemplate();

    public byte[] download(String repo) {
        return restTemplate.execute(
                String.format("%s/repos/%s/zipball", url, repo),
                HttpMethod.GET,
                null,
                clientHttpResponse -> clientHttpResponse.getBody().readAllBytes()
        );
    }
}

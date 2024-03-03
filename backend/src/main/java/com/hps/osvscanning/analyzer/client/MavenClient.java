package com.hps.osvscanning.analyzer.client;

import com.hps.osvscanning.model.mvn.LibraryBulk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MavenClient {
    @Value("${url.mavenClient}")
    private String url;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public LibraryBulk getLibraryBulk(String q, String core, Integer rows, String wt) {
        return webClientBuilder.build().get()
                .uri(url, builder -> builder
                        .path("/solrsearch/select")
                        .queryParam("q",q)
                        .queryParam("core",core)
                        .queryParam("rows",rows)
                        .queryParam("wt",wt)
                        .build())
                .retrieve().bodyToMono(LibraryBulk.class).block();
    }
}

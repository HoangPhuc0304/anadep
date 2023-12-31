package com.hps.osvscanning.client;

import com.hps.osvscanning.model.LibraryVersion;
import com.hps.osvscanning.model.osv.VulnerabilityList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OsvClient {
    @Value("${url.osvClient}")
    private String url;
    @Autowired
    private WebClient.Builder webClientBuilder;

    public VulnerabilityList getVulnerability(@RequestBody LibraryVersion libraryVersion) {
        return webClientBuilder.build().post()
                .uri(url, builder -> builder.path("/v1/query").build())
                .bodyValue(libraryVersion)
                .retrieve().bodyToMono(VulnerabilityList.class).block();
    }
}

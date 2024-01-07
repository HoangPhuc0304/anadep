package com.hps.osvscanning.client;

import com.hps.osvscanning.model.osv.LibraryOSVBatchRequest;
import com.hps.osvscanning.model.osv.LibraryOSVRequest;
import com.hps.osvscanning.model.osv.VulnerabilityOSVBatchResponse;
import com.hps.osvscanning.model.osv.VulnerabilityOSVResponse;
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

    public VulnerabilityOSVResponse getVulnerability(@RequestBody LibraryOSVRequest libraryOSVRequest) {
        return webClientBuilder.build().post()
                .uri(url, builder -> builder.path("/v1/query").build())
                .bodyValue(libraryOSVRequest)
                .retrieve().bodyToMono(VulnerabilityOSVResponse.class).block();
    }

    public VulnerabilityOSVBatchResponse getVulnerabilityBulk(@RequestBody LibraryOSVBatchRequest libraryOSVBatchRequest) {
        return webClientBuilder.build().post()
                .uri(url, builder -> builder.path("/v1/querybatch").build())
                .bodyValue(libraryOSVBatchRequest)
                .retrieve().bodyToMono(VulnerabilityOSVBatchResponse.class).block();
    }
}

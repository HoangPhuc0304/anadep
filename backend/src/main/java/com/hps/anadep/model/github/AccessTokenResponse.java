package com.hps.anadep.model.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenResponse {
    private String accessToken;
    private String scope;
    private String tokenType;
}

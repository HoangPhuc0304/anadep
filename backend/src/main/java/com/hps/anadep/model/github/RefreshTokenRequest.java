package com.hps.anadep.model.github;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private UUID userId;
    private String refreshToken;
}

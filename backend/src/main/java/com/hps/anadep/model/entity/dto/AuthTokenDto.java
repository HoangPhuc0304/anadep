package com.hps.anadep.model.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthTokenDto {

    @NotNull
    private UUID userId;

    @NotEmpty
    private String githubToken;

    @NotEmpty
    private String refreshToken;
}

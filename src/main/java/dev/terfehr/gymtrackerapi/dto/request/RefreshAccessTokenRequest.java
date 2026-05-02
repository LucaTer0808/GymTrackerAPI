package dev.terfehr.gymtrackerapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshAccessTokenRequest(
        @NotBlank(message = "The passed token must not be null or an empty string.")
        String refreshToken
) {}

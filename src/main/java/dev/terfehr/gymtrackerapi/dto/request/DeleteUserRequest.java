package dev.terfehr.gymtrackerapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteUserRequest(
        @NotBlank(message = "The current password must not be null or an empty string")
        String currentPassword
) {}

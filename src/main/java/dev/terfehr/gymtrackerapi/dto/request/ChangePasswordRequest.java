package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Password;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(
        @NotBlank(message = "The old password must not be null or an empty string")
        String currentPassword,

        @NotBlank(message = "The new password must not be null or an empty string")
        @Password(message = "The new password does not satisfy the safety requirements")
        String password,

        @NotBlank(message = "The new password must not be null or an empty string")
        @Password(message = "The new password does not satisfy the safety requirements")
        String password2
) {}

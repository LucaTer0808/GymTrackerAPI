package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import dev.terfehr.gymtrackerapi.annotation.Trim;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestPasswordResetRequest(
        @Trim
        @ToLowercase
        @NotBlank(message = "The provided email must not be null or an empty string")
        @Email(message = "Please provide a valid email that represents a user")
        String email
) {}

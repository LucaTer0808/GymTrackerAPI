package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestPasswordResetRequest(
        @Trim
        @ToLowercase
        @NotNull(message = "The provided email must not be null")
        @Email(message = "Please provide a valid email that represents a user")
        @Size(min = 1, max = User.MAX_EMAIL_LENGTH, message = "The provided email must not be longer than " +  User.MAX_EMAIL_LENGTH)
        String email
) {}

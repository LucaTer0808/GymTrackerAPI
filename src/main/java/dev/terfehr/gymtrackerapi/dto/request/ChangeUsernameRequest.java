package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeUsernameRequest(
        @Trim
        @ToLowercase
        @NotNull(message = "The username can not be null!")
        @Size(min = User.MIN_USERNAME_LENGTH, max = User.MAX_USERNAME_LENGTH, message = "The length of the username must be between " + User.MIN_USERNAME_LENGTH + " and " + User.MAX_USERNAME_LENGTH + " characters.")
        String username,

        @NotBlank(message = "The given password must not be null!")
        String currentPassword
) {}

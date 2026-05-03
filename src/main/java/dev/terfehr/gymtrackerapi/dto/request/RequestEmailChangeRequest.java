package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestEmailChangeRequest(
        @NotBlank(message = "The current password must not be null or an empty string!")
        String currentPassword,

        @Trim
        @ToLowercase
        @NotBlank(message = "The email address can not be null or an empty string!")
        @Email(message = "The email address must be in a valid format")
        @Size(max = User.MAX_EMAIL_LENGTH, message = "The email address must be no longer than " + User.MAX_EMAIL_LENGTH + " long.")
        String email
) {}

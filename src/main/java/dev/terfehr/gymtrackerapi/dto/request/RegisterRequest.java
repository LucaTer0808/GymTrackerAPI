package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Password;
import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NullMarked;

public record RegisterRequest(
        @Trim
        @NotBlank(message = "The first name can not be null or an empty string!")
        @Size(max = User.MAX_NAME_LENGTH, message = "The first name can be at maximum " + User.MAX_NAME_LENGTH + " characters long.")
        String firstName,

        @Trim
        @NotBlank(message = "The last name can not be null or an empty string!")
        @Size(max = User.MAX_NAME_LENGTH, message = "The last name can be at maximum " + User.MAX_NAME_LENGTH + " characters long.")
        String lastName,

        @Trim
        @ToLowercase
        @NotBlank(message = "The username can not be null!")
        @Size(min = User.MIN_USERNAME_LENGTH, max = User.MAX_USERNAME_LENGTH, message = "The length of the username must be between " + User.MIN_USERNAME_LENGTH + " and " + User.MAX_USERNAME_LENGTH + " characters.")
        String username,

        @Trim
        @ToLowercase
        @NotBlank(message = "The email address can not be null or an empty string!")
        @Email(message = "The email address must be in a valid format")
        @Size(max = User.MAX_EMAIL_LENGTH, message = "The email address must be no longer than " + User.MAX_EMAIL_LENGTH + " long.")
        String email,

        @Password(message = "The password is too weak")
        String password
) {}

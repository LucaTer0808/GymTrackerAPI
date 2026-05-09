package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Password;
import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.constraints.*;
import org.jspecify.annotations.NullMarked;

public record RegisterRequest(
        @Trim
        @NotNull(message = "The first name can not be null!")
        @Size(min = 1, max = User.MAX_NAME_LENGTH, message = "The first name can be at maximum " + User.MAX_NAME_LENGTH + " characters long.")
        String firstName,

        @Trim
        @NotNull(message = "The last name can not be null!")
        @Size(min = 1, max = User.MAX_NAME_LENGTH, message = "The last name can be at maximum " + User.MAX_NAME_LENGTH + " characters long.")
        String lastName,

        @Trim
        @ToLowercase
        @NotNull(message = "The username can not be null!")
        @Size(min = User.MIN_USERNAME_LENGTH, max = User.MAX_USERNAME_LENGTH, message = "The length of the username must be between " + User.MIN_USERNAME_LENGTH + " and " + User.MAX_USERNAME_LENGTH + " characters.")
        String username,

        @Trim
        @ToLowercase
        @NotNull(message = "The email address can not be null!")
        @Email(message = "The email address must be in a valid format")
        @Size(min = 1, max = User.MAX_EMAIL_LENGTH, message = "The email address must be no longer than " + User.MAX_EMAIL_LENGTH + " long.")
        String email,

        @Password(message = "The password is too weak")
        String password,

        @Password(message = "The password is too weak")
        String password2
) {}

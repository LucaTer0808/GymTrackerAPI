package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Password;
import jakarta.validation.constraints.NotBlank;

public record ConfirmPasswordResetRequest(
        @NotBlank(message = "Both passwords can not be null or an empty string")
        @Password(message = "The given password is too weak!")
        String password,

        @NotBlank(message = "Both passwords can not be null or an empty string")
        @Password(message = "The given password is too weak")
        String password2
) {
}

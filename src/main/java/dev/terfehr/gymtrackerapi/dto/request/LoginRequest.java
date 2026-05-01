package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.ToLowercase;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @ToLowercase
        @NotBlank(message = "The username for this request must contain something valid")
        String username,

        @NotBlank(message = "The password for this request must contain something valid.")
        String password)
{}

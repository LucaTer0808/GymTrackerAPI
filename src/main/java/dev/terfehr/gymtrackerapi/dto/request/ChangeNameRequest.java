package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.User;
import jakarta.validation.constraints.Size;

public record ChangeNameRequest(
        @Trim
        @Size(min = 1, max = User.MAX_NAME_LENGTH, message = "The first name can be at maximum " + User.MAX_NAME_LENGTH + " characters long.")
        String newFirstName,

        @Trim
        @Size(min = 1, max = User.MAX_NAME_LENGTH, message = "The last name can be at maximum " + User.MAX_NAME_LENGTH + " characters long.")
        String newLastName
) {}

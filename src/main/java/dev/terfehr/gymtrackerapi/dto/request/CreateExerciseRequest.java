package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.Exercise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateExerciseRequest(
        @Trim
        @NotNull(message = "The name of the exercise must not be null!")
        @Size(min = 1, max = Exercise.MAX_NAME_LENGTH, message = "The name of the exercise must be no longer than " + Exercise.MAX_NAME_LENGTH + " characters long")
        String name
) {}

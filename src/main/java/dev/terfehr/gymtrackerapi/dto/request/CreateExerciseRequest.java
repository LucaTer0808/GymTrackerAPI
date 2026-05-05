package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.model.Exercise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateExerciseRequest(
        @NotBlank(message = "The name of the exercise must not be null or an empty string!")
        @Size(max = Exercise.MAX_NAME_LENGTH, message = "The name of the exercise must be no longer than " + Exercise.MAX_NAME_LENGTH + " characters long")
        String name
) {}

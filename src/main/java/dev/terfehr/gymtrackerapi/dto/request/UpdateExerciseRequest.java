package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.Exercise;
import jakarta.validation.constraints.Size;

public record UpdateExerciseRequest(
        @Trim
        @Size(min = 1, max = Exercise.MAX_NAME_LENGTH, message = "The name of the exercise must not be longer than " + Exercise.MAX_NAME_LENGTH)
        String name
) {
}

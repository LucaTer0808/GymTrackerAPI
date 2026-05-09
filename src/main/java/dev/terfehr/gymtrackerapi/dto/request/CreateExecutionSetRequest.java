package dev.terfehr.gymtrackerapi.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateExecutionSetRequest(
        @NotNull(message = "Please include a valid number for your set in the request!")
        @Positive(message = "Please only use positive numbers to enumerate the sets in your request!")
        Integer numberInExecution,

        @NotNull(message = "Please include a valid number of repetitions in your set!")
        @Positive(message = "Please only use positive numbers for the amount of repetitions in your set!")
        Integer reps,

        @NotNull(message = "Please include a valid number to describe the weight you lifted during your set!")
        @Positive(message = "Please only use positive numbers to describe the weight you lifted during your set!")
        Double weight
) {
}

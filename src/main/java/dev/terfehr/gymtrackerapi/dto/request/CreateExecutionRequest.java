package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.ValidExecutionSetNumbers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateExecutionRequest(
        @NotNull(message = "Please include the sets of your execution in the request!")
        @Size(min = 1, message = "Please include at least one set in your request!")
        @ValidExecutionSetNumbers(message = "The list of sets does not contain a valid order of numbers within the execution")
        List<@Valid CreateExecutionSetRequest> executionSets
) {}

package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.Split;
import jakarta.validation.constraints.Size;

public record ChangeSplitRequest(
        @Trim
        @Size(min = 1, max = Split.MAX_NAME_LENGTH, message = "The new name of the spilt can be no longer than " + Split.MAX_NAME_LENGTH)
        String name
) {}

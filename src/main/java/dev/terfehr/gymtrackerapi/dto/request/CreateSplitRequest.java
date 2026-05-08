package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.Day;
import dev.terfehr.gymtrackerapi.model.Split;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record CreateSplitRequest(
        @Trim
        @NotBlank(message = "The name of the split must not be null or an empty string!")
        @Size(max = Split.MAX_NAME_LENGTH, message = "The split name must not be longer than " + Split.MAX_NAME_LENGTH)
        String name,

        @NotNull(message = "Please also include the names of the days in the request")
        @UniqueElements(message = "There must not be two days in the split with the same name!")
        @Size(min = 1, message = "Please register at least one day for the split")
        List<
                @NotBlank(message = "The name of each day must not be null or an empty string!")
                @Size(max = Day.MAX_NAME_LENGTH, message = "The name of the day must not be longer than " + Day.MAX_NAME_LENGTH)
                String
                > dayNames
) {
}

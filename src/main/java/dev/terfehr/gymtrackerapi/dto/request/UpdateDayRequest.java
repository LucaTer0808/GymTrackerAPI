package dev.terfehr.gymtrackerapi.dto.request;

import dev.terfehr.gymtrackerapi.annotation.Trim;
import dev.terfehr.gymtrackerapi.model.Day;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

public record UpdateDayRequest(
        @Trim
        @Size(min = 1, max = Day.MAX_NAME_LENGTH, message = "The name of the day must not be longer than " + Day.MAX_NAME_LENGTH)
        String name,

        @UniqueElements(message = "Each exercise can only be added once to the workout day!")
        @Size(min = 1, message = "Please add at least one exercise to your workout day!")
        List<
            @NotNull(message = "None of the IDs of the exercises can be null!")
                    Long> exerciseIds
) {}

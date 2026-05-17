package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Execution;
import dev.terfehr.gymtrackerapi.model.Exercise;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NullMarked
public record ExerciseDTO(long id, String name, @Nullable ZonedDateTime dateOfLatestExecution, List<ExecutionDTO> executions) {
    public ExerciseDTO(Exercise exercise, @Nullable ZonedDateTime dateOfLatestExecution) {
        Long exerciseId = exercise.getId();
        assert exerciseId != null;

        List<ExecutionDTO> executions = new ArrayList<>();

        for (Execution execution : exercise.getExecutions()) {
            executions.add(new ExecutionDTO(execution));
        }

        this(exerciseId, exercise.getName(), dateOfLatestExecution, executions);
    }
}

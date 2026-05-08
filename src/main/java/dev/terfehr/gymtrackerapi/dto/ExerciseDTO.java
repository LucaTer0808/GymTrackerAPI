package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Execution;
import dev.terfehr.gymtrackerapi.model.Exercise;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public record ExerciseDTO(long id, String name, List<ExecutionDTO> executions) {
    public ExerciseDTO(Exercise exercise) {
        Long exerciseId = exercise.getId();
        assert exerciseId != null;

        List<ExecutionDTO> executions = new ArrayList<>();

        for (Execution execution : exercise.getExecutions()) {
            executions.add(new ExecutionDTO(execution));
        }

        this(exerciseId, exercise.getName(), executions);
    }
}

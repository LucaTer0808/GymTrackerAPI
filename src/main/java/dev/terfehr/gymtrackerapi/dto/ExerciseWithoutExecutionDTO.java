package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Exercise;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ExerciseWithoutExecutionDTO(long id, String name) {
    public ExerciseWithoutExecutionDTO(Exercise exercise) {
        Long id =  exercise.getId();
        assert id != null; // dto is only used after persisting the object!
        this(id, exercise.getName());
    }
}

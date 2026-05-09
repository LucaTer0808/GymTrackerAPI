package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.ExecutionSet;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ExecutionSetDTO(Long id, int numberInExecution, double weight, int reps) {
    public ExecutionSetDTO(ExecutionSet executionSet) {
        Long executionSetId = executionSet.getId();
        assert  executionSetId != null;

        this(executionSet.getId(), executionSet.getNumberInExecution(), executionSet.getWeight(), executionSet.getReps());
    }
}

package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Execution;
import dev.terfehr.gymtrackerapi.model.ExecutionSet;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NullMarked
public record ExecutionDTO(long id, LocalDate date, List<ExecutionSetDTO> executionSets) {
    public ExecutionDTO(Execution execution) {
        List<ExecutionSetDTO> executionSets = new ArrayList<>();

        for (ExecutionSet executionSet : execution.getExecutionSets()) {
            executionSets.add(new ExecutionSetDTO(executionSet));
        }

        this(execution.getId(), execution.getDate(), executionSets);
    }
}

package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Execution;

public record ExecutionDTO() {
    public ExecutionDTO(Execution execution) {
        this();
    }
}

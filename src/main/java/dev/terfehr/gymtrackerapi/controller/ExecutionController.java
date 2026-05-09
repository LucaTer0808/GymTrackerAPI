package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.ExecutionDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateExecutionRequest;
import dev.terfehr.gymtrackerapi.dto.request.UpdateExecutionRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.ExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@NullMarked
@AllArgsConstructor
@Tag(name = "Executions", description = "Endpoints for managing exercise history and performance data.")
public class ExecutionController {

    private final ExecutionService executionService;

    @Operation(
            summary = "Record a new exercise execution",
            description = "Logs a completed exercise session, including all sets, weights, and repetitions for a specific exercise."
    )
    @PostMapping("/exercises/{exerciseId}/executions")
    public ResponseEntity<ExecutionDTO> createExecution(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long exerciseId,
            @RequestBody @Valid CreateExecutionRequest request) {
        ExecutionDTO dto = executionService.createExecution(
                authUser,
                exerciseId,
                request.executionSets()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get execution history for an exercise",
            description = "Retrieves a list of all past performances for a specific exercise belonging to the authenticated user."
    )
    @GetMapping("/exercises/{exerciseId}/executions")
    public ResponseEntity<List<ExecutionDTO>> getExecutions(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long exerciseId) {
        List<ExecutionDTO> dto = executionService.getExecutions(
                authUser,
                exerciseId
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update an execution entry",
            description = "Allows modifying the date or the individual sets (weight/reps) of an existing execution record."
    )
    @PatchMapping("/executions/{executionId}")
    public ResponseEntity<ExecutionDTO> updateExecution(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long executionId,
            @RequestBody @Valid UpdateExecutionRequest request) {
        ExecutionDTO dto = executionService.updateExecution(
                authUser,
                executionId,
                request.date(),
                request.executionSets()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete an execution record",
            description = "Permanently removes a specific execution and its associated sets from the exercise history."
    )
    @DeleteMapping("/executions/{executionId}")
    public ResponseEntity<Void> deleteExecution(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long executionId) {
        executionService.deleteExecution(
                authUser,
                executionId
        );

        return ResponseEntity.noContent().build();
    }
}
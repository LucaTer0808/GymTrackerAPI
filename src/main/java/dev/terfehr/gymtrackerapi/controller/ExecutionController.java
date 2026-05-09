package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.ExecutionDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateExecutionRequest;
import dev.terfehr.gymtrackerapi.dto.request.UpdateExecutionRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.ExecutionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@NullMarked
@AllArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping("/exercises/{exerciseId}/executions")
    public ResponseEntity<ExecutionDTO> createExecution(@AuthenticationPrincipal User authUser,
                                                        @PathVariable long exerciseId,
                                                        @RequestBody @Valid CreateExecutionRequest request) {
        ExecutionDTO dto = executionService.createExecution(
                authUser,
                exerciseId,
                request.executionSets()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/exercises/{exerciseId}/executions")
    public ResponseEntity<List<ExecutionDTO>> getExecutions(@AuthenticationPrincipal User authUser, @PathVariable long exerciseId) {
        List<ExecutionDTO> dto = executionService.getExecutions(
                authUser,
                exerciseId
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/executions/{executionId}")
    public ResponseEntity<ExecutionDTO> updateExecution(@AuthenticationPrincipal User authUser,
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

    @DeleteMapping("/executinos/{executionId}")
    public ResponseEntity<Void> deleteExecution(@AuthenticationPrincipal User authUser, @PathVariable long executionId) {
        executionService.deleteExecution(
                authUser,
                executionId
        );

        return ResponseEntity.noContent().build();
    }
}

package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.ExecutionDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateExecutionRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.ExecutionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NullMarked
@AllArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping("/exercises/{exerciseId}/executions")
    public ResponseEntity<ExecutionDTO> createExecution(@AuthenticationPrincipal User user,
                                                        @PathVariable Long exerciseId,
                                                        @RequestBody @Valid CreateExecutionRequest request) {
        ExecutionDTO dto = executionService.createExecution(
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}

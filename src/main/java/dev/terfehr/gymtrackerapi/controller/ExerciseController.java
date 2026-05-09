package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.ExerciseDTO;
import dev.terfehr.gymtrackerapi.dto.ExerciseWithoutExecutionDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateExerciseRequest;
import dev.terfehr.gymtrackerapi.dto.request.UpdateExerciseRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.ExerciseService;
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
@RequestMapping("/exercises")
@Tag(name = "Exercises", description = "Endpoints for managing the catalog of exercises available to the user.")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Operation(
            summary = "Create a new exercise",
            description = "Adds a custom exercise to the user's account. This exercise can later be assigned to training days."
    )
    @PostMapping
    public ResponseEntity<ExerciseWithoutExecutionDTO> createExercise(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid CreateExerciseRequest request) {
        ExerciseWithoutExecutionDTO dto = exerciseService.createExercise(
                authUser,
                request.name()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "List all exercises",
            description = "Retrieves all exercises created by the authenticated user, including basic details."
    )
    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getExercises(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser) {
        List<ExerciseDTO> dto = exerciseService.getExercises(
                authUser
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update exercise details",
            description = "Allows changing the name or properties of an existing exercise."
    )
    @PatchMapping("/{exerciseId}")
    public ResponseEntity<ExerciseWithoutExecutionDTO> updateExercise(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long exerciseId,
            @RequestBody @Valid UpdateExerciseRequest request) {
        ExerciseWithoutExecutionDTO dto = exerciseService.updateExercise(
                authUser,
                exerciseId,
                request.name()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete an exercise",
            description = "Removes the exercise from the user's account. Note: This might affect training days or history linked to this exercise depending on cascade settings."
    )
    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long exerciseId) {
        exerciseService.deleteExercise(
                authUser,
                exerciseId
        );

        return ResponseEntity.noContent().build();
    }
}
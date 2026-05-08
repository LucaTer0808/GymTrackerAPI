package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.ExerciseDTO;
import dev.terfehr.gymtrackerapi.dto.ExerciseWithoutExecutionDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateExerciseRequest;
import dev.terfehr.gymtrackerapi.dto.request.UpdateExerciseRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercises")
@NullMarked
@AllArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseWithoutExecutionDTO> createExercise(@AuthenticationPrincipal User authUser, @RequestBody @Valid CreateExerciseRequest request) {
        ExerciseWithoutExecutionDTO dto = exerciseService.createExercise(
                authUser,
                request.name()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ExerciseDTO> getExercise(@AuthenticationPrincipal User authUser, @PathVariable long exerciseId) {
        ExerciseDTO dto = exerciseService.getExercise(
                authUser,
                exerciseId
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{exerciseId}")
    public ResponseEntity<ExerciseWithoutExecutionDTO> updateExercise(@AuthenticationPrincipal User authUser,
                                                      @PathVariable long exerciseId,
                                                      @RequestBody @Valid UpdateExerciseRequest request) {
        ExerciseWithoutExecutionDTO dto = exerciseService.updateExercise(
                authUser,
                exerciseId,
                request.name()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExercise(@AuthenticationPrincipal User authUser, @PathVariable long exerciseId) {
        exerciseService.deleteExercise(
                authUser,
                exerciseId
        );

        return ResponseEntity.noContent().build();
    }
}

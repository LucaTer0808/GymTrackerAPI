package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.ExerciseDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateExerciseRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercises")
@NullMarked
@AllArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@AuthenticationPrincipal User authUser, @RequestBody @Valid CreateExerciseRequest request) {
        ExerciseDTO dto = exerciseService.createExercise(authUser, request.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}

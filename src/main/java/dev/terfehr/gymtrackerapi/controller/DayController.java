package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.DayDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateDayRequest;
import dev.terfehr.gymtrackerapi.dto.request.UpdateDayRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.DayService;
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

@RestController
@SecurityRequirement(name = "bearerAuth")
@NullMarked
@AllArgsConstructor
@RequestMapping("/days")
@Tag(name = "Days", description = "Managing the training days belonging to your workout split.")
public class DayController {

    private final DayService dayService;

    @Operation(
            summary = "Create a new training day",
            description = "Adds a specific day to the user's current split and links the provided exercises to it."
    )
    @PostMapping
    public ResponseEntity<DayDTO> createDay(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid CreateDayRequest request) {
        DayDTO dto = dayService.createDay(
                authUser,
                request.name(),
                request.exerciseIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get day details",
            description = "Retrieves information about a specific day, including its name and the exercises assigned to it."
    )
    @GetMapping("/{dayId}")
    public ResponseEntity<DayDTO> getDay(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long dayId) {
        DayDTO dto = dayService.getDay(
                authUser,
                dayId
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update a training day",
            description = "Allows updating the name of the day or changing the list of associated exercise IDs."
    )
    @PatchMapping("/{dayId}")
    public ResponseEntity<DayDTO> updateDay(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long dayId,
            @RequestBody @Valid UpdateDayRequest request) {
        DayDTO dto = dayService.updateDay(
                authUser,
                dayId,
                request.name(),
                request.exerciseIds()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete a training day",
            description = "Permanently removes a training day from the user's split."
    )
    @DeleteMapping("/{dayId}")
    public ResponseEntity<Void> deleteDay(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @PathVariable long dayId) {
        dayService.deleteDay(
                authUser,
                dayId
        );

        return ResponseEntity.noContent().build();
    }
}
package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.annotation.openapi.ApiBadRequestResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiForbiddenResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiInternalServerErrorResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiNotFoundResponse;
import dev.terfehr.gymtrackerapi.dto.SplitDTO;
import dev.terfehr.gymtrackerapi.dto.request.ChangeSplitRequest;
import dev.terfehr.gymtrackerapi.dto.request.CreateSplitRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.SplitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/splits")
@Tag(name = "Splits", description = "Endpoints for managing your overall workout split and its structure.")
public class SplitController {

    private final SplitService splitService;

    @Operation(
            summary = "Create a new workout split",
            description = "Initializes a workout split (e.g., 'Push/Pull/Legs') and creates the initial set of training days."
    )
    @ApiResponse(responseCode = "201", description = "Workout split created successfully", content = @Content(schema = @Schema(implementation = SplitDTO.class)))
    @ApiBadRequestResponse
    @ApiForbiddenResponse
    @ApiInternalServerErrorResponse
    @PostMapping
    public ResponseEntity<SplitDTO> createSplit(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid CreateSplitRequest request) {
        SplitDTO dto = splitService.createSplit(
                authUser,
                request.name(),
                request.dayNames()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get current split",
            description = "Retrieves the full structure of the authenticated user's current workout split, including all training days."
    )
    @ApiResponse(responseCode = "200", description = "Current workout split", content = @Content(schema = @Schema(implementation = SplitDTO.class)))
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    @ApiInternalServerErrorResponse
    @GetMapping
    public ResponseEntity<SplitDTO> getSplit(@Parameter(hidden = true) @AuthenticationPrincipal User authUser) {
        SplitDTO dto = splitService.getSplit(authUser);

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update split name",
            description = "Renames the user's current workout split without modifying its associated training days."
    )
    @ApiResponse(responseCode = "200", description = "Workout split renamed successfully", content = @Content(schema = @Schema(implementation = SplitDTO.class)))
    @ApiBadRequestResponse
    @ApiForbiddenResponse
    @ApiNotFoundResponse
    @ApiInternalServerErrorResponse
    @PatchMapping("/name")
    public ResponseEntity<SplitDTO> changeSplitName(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid ChangeSplitRequest request) {
        SplitDTO dto = splitService.changeSplitName(
                authUser,
                request.name()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete workout split",
            description = "Permanently removes the current workout split and all associated training days and exercise slots."
    )
    @ApiResponse(responseCode = "204", description = "Workout split deleted successfully", content = @Content)
    @ApiForbiddenResponse
    @ApiInternalServerErrorResponse
    @DeleteMapping
    public ResponseEntity<Void> deleteSplit(@Parameter(hidden = true) @AuthenticationPrincipal User authUser) {
        splitService.deleteSplit(authUser);

        return ResponseEntity.noContent().build();
    }
}
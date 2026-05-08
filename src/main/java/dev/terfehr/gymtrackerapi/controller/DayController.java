package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.DayDTO;
import dev.terfehr.gymtrackerapi.dto.request.CreateDayRequest;
import dev.terfehr.gymtrackerapi.dto.request.UpdateDayRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.DayService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@NullMarked
@AllArgsConstructor
@RequestMapping("/days")
public class DayController {

    private final DayService dayService;

    @PostMapping
    public ResponseEntity<DayDTO> createDay(@AuthenticationPrincipal User authUser, @RequestBody @Valid CreateDayRequest request) {
        DayDTO dto = dayService.createDay(
                authUser,
                request.name(),
                request.exerciseIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{dayId}")
    public ResponseEntity<DayDTO> getDay(@AuthenticationPrincipal User authUser, @PathVariable long dayId) {
        DayDTO dto = dayService.getDay(
                authUser,
                dayId
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{dayId}")
    public ResponseEntity<DayDTO> updateDay(@AuthenticationPrincipal User authUser, @PathVariable long dayId, @RequestBody @Valid UpdateDayRequest request) {
        DayDTO dto = dayService.updateDay(
                authUser,
                dayId,
                request.name(),
                request.exerciseIds()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("(/{dayId}")
    public ResponseEntity<Void> deleteDay(@AuthenticationPrincipal User authUser, @PathVariable long dayId) {
        dayService.deleteDay(
                authUser,
                dayId
        );

        return ResponseEntity.noContent().build();
    }
}

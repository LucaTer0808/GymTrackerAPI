package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.SplitDTO;
import dev.terfehr.gymtrackerapi.dto.request.ChangeSplitRequest;
import dev.terfehr.gymtrackerapi.dto.request.CreateSplitRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.SplitService;
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
@RequestMapping("/splits")
public class SplitController {

    private final SplitService splitService;

    @PostMapping
    public ResponseEntity<SplitDTO> createSplit(@AuthenticationPrincipal User authUser, @RequestBody @Valid CreateSplitRequest request) {
        SplitDTO dto = splitService.createSplit(
                authUser,
                request.name(),
                request.dayNames()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<SplitDTO> getSplit(@AuthenticationPrincipal User authUser) {
        SplitDTO dto = splitService.getSplit(authUser);

        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/name")
    public ResponseEntity<SplitDTO> changeSplitName(@AuthenticationPrincipal User authUser, @RequestBody @Valid ChangeSplitRequest request) {
        SplitDTO dto = splitService.changeSplitName(
                authUser,
                request.name()
        );

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSplit(@AuthenticationPrincipal User authUser) {
        splitService.deleteSplit(authUser);

        return ResponseEntity.noContent().build();

    }
}

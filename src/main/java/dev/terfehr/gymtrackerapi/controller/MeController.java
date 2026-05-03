package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.dto.request.ChangePasswordRequest;
import dev.terfehr.gymtrackerapi.dto.request.ChangeUsernameRequest;
import dev.terfehr.gymtrackerapi.dto.request.RequestEmailChangeRequest;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@NullMarked
@RestController
@RequestMapping("/users/me")
@AllArgsConstructor
public class MeController {

    private final UserService userService;

    @PatchMapping("/username")
    public ResponseEntity<UserDTO> changeUsername(@AuthenticationPrincipal User authUser, @RequestBody @Valid ChangeUsernameRequest request) {
        UserDTO dto = userService.changeUsername(
                authUser,
                request.username(),
                request.currentPassword()
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/password")
    public ResponseEntity<UserDTO> changePassword(@AuthenticationPrincipal User authUser, @RequestBody @Valid ChangePasswordRequest request) {
        UserDTO dto = userService.changePassword(
                authUser,
                request.currentPassword(),
                request.password(),
                request.password2()
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/email/request")
    public ResponseEntity<UserDTO> requestEmailChange(@AuthenticationPrincipal User authUser, @RequestBody @Valid RequestEmailChangeRequest request) {
        UserDTO dto = userService.requestEmailChange(
                authUser,
                request.currentPassword(),
                request.email()
        );

        return ResponseEntity.ok(dto);
    }
}

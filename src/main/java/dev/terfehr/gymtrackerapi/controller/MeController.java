package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.dto.request.*;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@NullMarked
@AllArgsConstructor
@RequestMapping("/users/me")
@Tag(name = "Me", description = "Endpoints for managing your own profile and sensitive account information.")
public class MeController {

    private final UserService userService;

    @Operation(
            summary = "Change username",
            description = "Updates the unique username of the authenticated user. Requires the current password for verification."
    )
    @PatchMapping("/username")
    public ResponseEntity<UserDTO> changeUsername(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid ChangeUsernameRequest request) {
        UserDTO dto = userService.changeUsername(
                authUser,
                request.username(),
                request.currentPassword()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Change password",
            description = "Updates the user's password. Requires the current password and a matching confirmation of the new password."
    )
    @PatchMapping("/password")
    public ResponseEntity<UserDTO> changePassword(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid ChangePasswordRequest request) {
        UserDTO dto = userService.changePassword(
                authUser,
                request.currentPassword(),
                request.password(),
                request.password2()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Request email change",
            description = "Triggers a request to change the user's email address. Typically followed by a verification step. Requires the current password."
    )
    @PatchMapping("/email/request")
    public ResponseEntity<UserDTO> requestEmailChange(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid RequestEmailChangeRequest request) {
        UserDTO dto = userService.requestEmailChange(
                authUser,
                request.currentPassword(),
                request.email()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update personal name",
            description = "Changes the first and last name displayed on the user's profile."
    )
    @PatchMapping("/name")
    public ResponseEntity<UserDTO> changeName(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid ChangeNameRequest request) {
        UserDTO dto = userService.changeName(
                authUser,
                request.newFirstName(),
                request.newLastName()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete account",
            description = "Permanently deletes the authenticated user's account and all associated data. This action is irreversible. Requires the current password."
    )
    @DeleteMapping
    public String deleteUser(
            @Parameter(hidden = true) @AuthenticationPrincipal User authUser,
            @RequestBody @Valid DeleteUserRequest request) {
        userService.deleteUser(authUser, request.currentPassword());

        return "Your account has been deleted!";
    }
}
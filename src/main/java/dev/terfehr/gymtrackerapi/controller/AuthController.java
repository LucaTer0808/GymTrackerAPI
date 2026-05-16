package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.annotation.openapi.ApiBadRequestResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiConflictResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiGoneResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiInternalServerErrorResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiNotFoundResponse;
import dev.terfehr.gymtrackerapi.annotation.openapi.ApiUnauthorizedResponse;
import dev.terfehr.gymtrackerapi.dto.LoginDTO;
import dev.terfehr.gymtrackerapi.dto.RefreshAccessTokenDTO;
import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.dto.request.*;
import dev.terfehr.gymtrackerapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@NullMarked
@RestController
@RequestMapping(AuthController.AUTH_PATH)
@AllArgsConstructor
@Tag(name = "Authentication", description = "Endpoints related to Authentication. No Bearer Token required.")
public class AuthController {

    public static final String AUTH_PATH = "/auth";
    public static final String VERIFY_PATH = "/verify";
    public static final String CONFIRM_PASSWORD_RESET_PATH = "/reset-password/verify";
    public static final String CONFIRM_EMAIL_CHANGE_PATH = "/email/verify";

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and triggers a verification email."
    )
    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiBadRequestResponse
    @ApiConflictResponse
    @ApiInternalServerErrorResponse
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterRequest request) {
        UserDTO dto = this.authService.registerUser(
                request.firstName(),
                request.lastName(),
                request.username(),
                request.email(),
                request.password(),
                request.password2()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Verify account",
            description = "Activates a user account using the verification code received via email."
    )
    @ApiResponse(responseCode = "200", description = "Account verified successfully", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiBadRequestResponse
    @ApiNotFoundResponse
    @ApiGoneResponse
    @ApiConflictResponse
    @ApiInternalServerErrorResponse
    @PatchMapping(VERIFY_PATH + "/{verificationCode}")
    public ResponseEntity<UserDTO> verify(@PathVariable String verificationCode) {
        UserDTO dto = this.authService.verifyUser(
                verificationCode
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "User Login",
            description = "Authenticates user credentials and returns access and refresh tokens."
    )
    @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginDTO.class)))
    @ApiBadRequestResponse
    @ApiUnauthorizedResponse
    @ApiNotFoundResponse
    @ApiInternalServerErrorResponse
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginRequest request) {
        LoginDTO dto = this.authService.login(
                request.username(),
                request.password()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @ApiResponse(responseCode = "200", description = "Access token refreshed successfully", content = @Content(schema = @Schema(implementation = RefreshAccessTokenDTO.class)))
    @ApiBadRequestResponse
    @ApiUnauthorizedResponse
    @ApiInternalServerErrorResponse
    @PostMapping("/access-token")
    public ResponseEntity<RefreshAccessTokenDTO> refreshAccessToken(@RequestBody @Valid RefreshAccessTokenRequest request) {
        RefreshAccessTokenDTO dto = this.authService.refreshAccessToken(
                request.refreshToken()
        );

        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Request password reset",
            description = "Sends a password reset link to the provided email address if it exists in the system."
    )
    @ApiResponse(responseCode = "200", description = "Password reset request processed", content = @Content(mediaType = "text/plain", schema = @Schema(type = "string")))
    @ApiBadRequestResponse
    @ApiInternalServerErrorResponse
    @PatchMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid RequestPasswordResetRequest request) {
        String message = this.authService.requestPasswordReset(
                request.email()
        );

        return ResponseEntity.ok(message);
    }

    @Operation(
            summary = "Confirm email change",
            description = "Verifies and finalizes the email change process using the code sent to the new email address."
    )
    @ApiResponse(responseCode = "200", description = "Email change confirmed successfully", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiBadRequestResponse
    @ApiNotFoundResponse
    @ApiGoneResponse
    @ApiConflictResponse
    @ApiInternalServerErrorResponse
    @PatchMapping(CONFIRM_EMAIL_CHANGE_PATH + "/{changeEmailCode}")
    public ResponseEntity<UserDTO> confirmEmailChange(@PathVariable String changeEmailCode) {
        UserDTO user = this.authService.confirmEmailChange(
                changeEmailCode
        );

        return ResponseEntity.ok(user);
    }
}
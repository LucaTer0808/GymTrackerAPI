package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.LoginDTO;
import dev.terfehr.gymtrackerapi.dto.RefreshAccessTokenDTO;
import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.dto.request.*;
import dev.terfehr.gymtrackerapi.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthController.AUTH_PATH)
@AllArgsConstructor
public class AuthController {

    public static final String AUTH_PATH = "/auth";
    public static final String VERIFY_PATH = "/verify";
    public static final String CONFIRM_PASSWORD_RESET_PATH = "/reset-password/verify";

    private final AuthService authService;

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

        return ResponseEntity.ok(dto);
    }

    @PatchMapping(VERIFY_PATH + "/{verificationCode}")
    public ResponseEntity<UserDTO> verify(@PathVariable String verificationCode) {
        UserDTO dto = this.authService.verifyUser(
                verificationCode
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody @Valid LoginRequest request) {
        LoginDTO dto = this.authService.login(
                request.username(),
                request.password()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/access-token")
    public ResponseEntity<RefreshAccessTokenDTO> refreshAccessToken(@RequestBody @Valid RefreshAccessTokenRequest request) {
        RefreshAccessTokenDTO dto = this.authService.refreshAccessToken(
                request.refreshToken()
        );

        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/password-reset/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid RequestPasswordResetRequest request) {
        String message = this.authService.requestPasswordReset(
                request.email()
        );

        return ResponseEntity.ok(message);
    }
}

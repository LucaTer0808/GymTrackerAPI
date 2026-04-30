package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.dto.request.RegisterRequest;
import dev.terfehr.gymtrackerapi.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthController.AUTH_PATH)
@AllArgsConstructor
public class AuthController {

    public static final String AUTH_PATH = "/auth";

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid RegisterRequest request) {
        return null;
    }
}

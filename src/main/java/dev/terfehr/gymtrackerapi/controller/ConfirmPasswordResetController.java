package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@NullMarked
@Controller
@RequestMapping(AuthController.AUTH_PATH)
@AllArgsConstructor
@Tag(name = "Password Reset Confirmation", description = "Template-serving endpoints for verifying the password reset.")
public class ConfirmPasswordResetController {

    private final AuthService authService;

    @Operation(
            summary = "Display password reset form",
            description = "Renders the HTML password reset page using the token provided in the reset email."
    )
    @GetMapping(AuthController.CONFIRM_PASSWORD_RESET_PATH + "/{passwordChangeCode}")
    public String confirmPasswordReset(
            @PathVariable String passwordChangeCode,
            @Parameter(hidden = true) Model model) {
        model.addAttribute("token", passwordChangeCode);
        return "reset-password";
    }

    @Operation(
            summary = "Process password reset",
            description = "Handles the form submission from the reset page to update the user's password in the database."
    )
    @PostMapping("/confirm-password-reset")
    public String processReset(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            @RequestParam("passwordConfirm") String passwordConfirm,
            @Parameter(hidden = true) Model model) {
        try {
            authService.confirmPasswordReset(token, password, passwordConfirm);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("token", token);
            model.addAttribute("error", e.getMessage());
        }
        return "reset-password";
    }
}
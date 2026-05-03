package dev.terfehr.gymtrackerapi.controller;

import dev.terfehr.gymtrackerapi.service.AuthService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@NullMarked
@Controller
@RequestMapping(AuthController.AUTH_PATH)
@AllArgsConstructor
public class ConfirmPasswordResetController {

    private final AuthService authService;

    @GetMapping(AuthController.CONFIRM_PASSWORD_RESET_PATH + "/{passwordChangeCode}")
    public String confirmPasswordReset(@PathVariable String passwordChangeCode, Model model) {
        model.addAttribute("token", passwordChangeCode);
        return "reset-password";
    }

    @PostMapping("/confirm-password-reset")
    public String processReset(@RequestParam("token") String token,
                               @RequestParam("password") String password,
                               @RequestParam("passwordConfirm") String passwordConfirm,
                               Model model) {
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

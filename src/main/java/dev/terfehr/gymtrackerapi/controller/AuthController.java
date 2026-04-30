package dev.terfehr.gymtrackerapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.AUTH_PATH)
public class AuthController {

    public static final String AUTH_PATH = "/auth";
}

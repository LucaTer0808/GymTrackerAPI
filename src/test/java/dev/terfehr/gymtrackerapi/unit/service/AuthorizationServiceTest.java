package dev.terfehr.gymtrackerapi.unit.service;

import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.security.AuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

public class AuthorizationServiceTest {

    private AuthorizationService authorizationService;
    private User user;

    @BeforeEach
    public void init() {
        this.authorizationService = new AuthorizationService();
        this.user = new User("Max", "Mustermann", "a_username", "a_email@email.com", "hashedPassword", "a_test_code");
    }

    @DisplayName("isVerified() test")
    @Test
    public void testIsVerified() {
        assertThat(authorizationService.isVerified(null)).isFalse();
        assertThat(authorizationService.isVerified(new Object())).isFalse();
        assertThat(authorizationService.isVerified(user)).isFalse();

        user.verify("a_test_code", Instant.now());

        assertThat(authorizationService.isVerified(user)).isTrue();
    }
}

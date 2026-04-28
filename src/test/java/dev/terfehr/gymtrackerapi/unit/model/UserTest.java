package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.exception.VerificationException;
import dev.terfehr.gymtrackerapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

public class UserTest {

    private User user;
    private final String testCode = "123456";
    private final String wrongTestCode = "654321";
    private final String email = "max.mustermann.de";

    @BeforeEach
    void setUp() {
        this.user = new User("Max", "Mustermann", email, "hashedPassword", testCode);
    }

    @Test
    @DisplayName("Enable with enabled user")
    void testEnableWithEnabledUser() {
        this.user.enable(testCode, Instant.now());

        assertThatThrownBy(() -> user.enable(testCode, Instant.now()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("is already enabled!");
    }

    @Test
    @DisplayName("Enable with wrong verification code")
    void testEnableWithWrongVerificationCode() {
        assertThatThrownBy(() -> user.enable(wrongTestCode, Instant.now()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The given verification code does not match the one of the user!");
    }

    @Test
    @DisplayName("Enabling with expired verification code")
    void testEnableWithExpiredVerificationCode() {
        assertThatThrownBy(() -> user.enable(testCode, Instant.now().plus(Duration.ofDays(2))))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The given verification code is already expired. Please apply for a renewal!");
    }

    @Test
    @DisplayName("Enable as it is intended to work")
    void testEnable() {
        assertThat(user.isEnabled()).isFalse();
        assertThat(user.getVerificationCode()).isEqualTo(testCode);
        assertThat(user.getReservedEmail()).isEqualTo(email);
        assertThat(user.getEmail()).isNull();

        user.enable(testCode, Instant.now());

        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getVerificationCodeExpiration()).isNull();
        assertThat(user.getVerificationCode()).isNull();
        assertThat(user.getReservedEmail()).isNull();
    }
}

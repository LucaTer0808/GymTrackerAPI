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
        this.user = new User("Max", "Mustermann", "a_username", email, "hashedPassword", testCode);
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

    @Test
    @DisplayName("requestPasswordChange as it is intended to work")
    void testRequestPasswordChange() {
        assertThat(user.getPasswordChangeCode()).isNull();
        assertThat(user.getPasswordChangeCodeExpiration()).isNull();

        user.requestPasswordChange(testCode);

        assertThat(user.getPasswordChangeCode()).isEqualTo(testCode);
        assertThat(user.getPasswordChangeCodeExpiration()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("verifyPasswordChange with a wrong test code")
    void testVerifyPasswordChangeWithWrongTestCode() {
        user.requestPasswordChange(testCode);

        assertThatThrownBy(() -> user.verifyPasswordChange("a_valid_password", wrongTestCode, Instant.now()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The given password change code does not match");
    }

    @Test
    @DisplayName("verifyPasswordChange with expired password change code")
    void testVerifyPasswordChangeWithExpiredTestCode() {
        user.requestPasswordChange(testCode);

        assertThatThrownBy(() -> user.verifyPasswordChange("a_valid_password", testCode, Instant.now().plus(Duration.ofDays(2))))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The given password change code has expired!");
    }

    @Test
    @DisplayName("verifyPasswordChange works as intended")
    void testVerifyPasswordChange() {
        user.requestPasswordChange(testCode);

        assertThat(user.getPassword()).isEqualTo("hashedPassword");
        assertThat(user.getPasswordChangeCode()).isNotNull();
        assertThat(user.getPasswordChangeCodeExpiration()).isAfter(Instant.now());

        user.verifyPasswordChange("a_valid_password", testCode, Instant.now());

        assertThat(user.getPassword()).isEqualTo("a_valid_password");
        assertThat(user.getPasswordChangeCode()).isNull();
        assertThat(user.getPasswordChangeCodeExpiration()).isNull();
    }

    @Test
    @DisplayName("requestEmailChange works as intended")
    void testRequestEmailChange() {
        user.enable(testCode, Instant.now());

        assertThat(user.getReservedEmail()).isNull();
        assertThat(user.getEmailChangeCode()).isNull();
        assertThat(user.getEmailChangeCodeExpiration()).isNull();

        user.requestEmailChange("a_valid_email", testCode);

        assertThat(user.getReservedEmail()).isEqualTo("a_valid_email");
        assertThat(user.getEmailChangeCode()).isEqualTo(testCode);
        assertThat(user.getEmailChangeCodeExpiration()).isAfter(Instant.now());
    }

    @Test
    @DisplayName("confirmEmailChange with wrong email change code")
    void testConfirmEmailChangeWithWrongEmailChangeCode() {
        user.enable(testCode, Instant.now());
        user.requestEmailChange("a_valid_email", testCode);
        assertThatThrownBy(() -> user.confirmEmailChange(wrongTestCode, Instant.now()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The given email change code does not match the one of the user!");
    }

    @Test
    @DisplayName("confirmEmailChange with expired email change code")
    void testConfirmEmailChangeWithExpiredEmailChangeCode() {
        user.enable(testCode, Instant.now());
        user.requestEmailChange("a_valid_email", testCode);
        assertThatThrownBy(() -> user.confirmEmailChange(testCode, Instant.now().plus(Duration.ofDays(2))))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The given email change code has expired! Please apply for a renewal!");
    }

    @Test
    @DisplayName("confirmEmailChange with disabled user")
    void testConfirmEmailChangeWithDisabledUser() {
        user.requestEmailChange("a_valid_email", testCode);
        assertThatThrownBy(() -> user.confirmEmailChange(testCode, Instant.now()))
                .isInstanceOf(VerificationException.class)
                .hasMessageContaining("The user is not yet verified and must not change his email until he is!");
    }

    @Test
    @DisplayName("changeName works as intended")
    void testChangeName() {
        user.changeName(null, null);

        assertThat(user.getFirstName()).isEqualTo("Max");
        assertThat(user.getLastName()).isEqualTo("Mustermann");

        user.changeName("bla", null);

        assertThat(user.getFirstName()).isEqualTo("bla");
        assertThat(user.getLastName()).isEqualTo("Mustermann");

        user.changeName("bla", "bla");

        assertThat(user.getFirstName()).isEqualTo("bla");
        assertThat(user.getLastName()).isEqualTo("bla");

        user.changeName("a", "b");

        assertThat(user.getFirstName()).isEqualTo("a");
        assertThat(user.getLastName()).isEqualTo("b");
    }
}

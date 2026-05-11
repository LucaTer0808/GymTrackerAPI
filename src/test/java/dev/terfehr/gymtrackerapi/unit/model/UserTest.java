package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.exception.VerificationException;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.Split;
import dev.terfehr.gymtrackerapi.model.User;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User createUser() {
        return new User(
                "Max",
                "Mustermann",
                "max12345",
                "max@mail.com",
                "hashedPw",
                "verifyCode"
        );
    }

    @Test
    void shouldCreateUserCorrectly() {
        User user = createUser();

        assertEquals("Max", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());
        assertEquals("max12345", user.getUsername());
        assertEquals("hashedPw", user.getPassword());
        assertEquals("ROLE_USER", user.getAuthorities().iterator().next().getAuthority());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isEnabled());
        assertFalse(user.isVerified());
    }

    @Test
    void shouldVerifyUserSuccessfully() {
        User user = createUser();

        Instant now = Instant.now();
        user.verify("verifyCode", now);

        assertTrue(user.isEnabled());
        assertEquals("max@mail.com", user.getEmail());
        assertNull(user.getReservedEmail());
        assertNull(user.getVerificationCode());
        assertNull(user.getVerificationCodeExpiration());
    }

    @Test
    void shouldThrowWhenVerificationCodeIsWrong() {
        User user = createUser();

        assertThrows(VerificationException.class, () ->
                user.verify("wrongCode", Instant.now())
        );
    }

    @Test
    void shouldThrowWhenVerificationExpired() {
        User user = createUser();

        Instant future = Instant.now().plus(User.REGISTRATION_EXPIRATION.plus(Duration.ofDays(2)));

        assertThrows(VerificationException.class, () ->
                user.verify("verifyCode", future)
        );
    }

    @Test
    void shouldRequestPasswordChange() {
        User user = createUser();

        user.requestPasswordChange("resetCode");

        assertThrows(VerificationException.class, () ->
                user.verifyPasswordChange("newPw", "wrong", Instant.now())
        );

        user.verifyPasswordChange("newPw", "resetCode", Instant.now());

        assertEquals("newPw", user.getPassword());
    }

    @Test
    void shouldChangeUsername() {
        User user = createUser();

        user.changeUsername("newName");

        assertEquals("newName", user.getUsername());
    }

    @Test
    void shouldChangeNamePartially() {
        User user = createUser();

        user.changeName("John", null);

        assertEquals("John", user.getFirstName());
        assertEquals("Mustermann", user.getLastName());

        user.changeName(null, "Doe");

        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void shouldAddExercise() {
        User user = createUser();

        Exercise exercise = user.addExercise("Bench Press");

        assertNotNull(exercise);
        assertEquals(1, user.getExercises().size());
    }

    @Test
    void shouldChangeSplit() {
        User user = createUser();

        Split split = user.changeSplit("Push Pull", List.of("Push", "Pull"));

        assertNotNull(split);
        assertEquals(split, user.getSplit());
    }

    @Test
    void shouldDeleteSplit() {
        User user = createUser();

        user.changeSplit("Push Pull", List.of("Push", "Pull"));
        user.deleteSplit();

        assertNull(user.getSplit());
    }

    @Test
    void shouldRequestAndConfirmEmailChange() {
        User user = createUser();
        user.verify("verifyCode",  Instant.now());

        user.requestEmailChange("new@mail.com", "emailCode");

        assertThrows(VerificationException.class, () ->
                user.confirmEmailChange("wrong", Instant.now())
        );

        user.confirmEmailChange("emailCode", Instant.now());

        assertEquals("new@mail.com", user.getEmail());
        assertNull(user.getEmailChangeCode());
        assertNull(user.getEmailChangeCodeExpiration());
        assertNull(user.getReservedEmail());
    }

    @Test
    void shouldDeleteExercise() {
        User user = createUser();

        Exercise exercise = user.addExercise("Squat");

        user.deleteExercise(exercise);

        assertEquals(0, user.getExercises().size());
    }
}
package dev.terfehr.gymtrackerapi.model;

import dev.terfehr.gymtrackerapi.exception.VerificationException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@NullMarked
@NoArgsConstructor
public class User implements UserDetails {

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_OWNER = "OWNER";
    public static final Duration REGISTRATION_EXPIRATION = Duration.ofDays(1);
    public static final Duration CHANGE_PASSWORD_DURATION = Duration.ofHours(1);
    public static final Duration CHANGE_EMAIL_DURATION = Duration.ofDays(1);
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,256}$"; // 1 UC, 1 LC, 8 chars, 1 Number, 1 extra char
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_USERNAME_LENGTH = 8;
    public static final int MAX_USERNAME_LENGTH = 50;
    public static final int MAX_EMAIL_LENGTH = 100;
    public static final int MAX_VERIFICATION_CODE_LENGTH = 50;
    public static final int MAX_CHANGE_PASSWORD_CODE_LENGTH = 50;
    public static final int MAX_EMAIL_CHANGE_CODE_LENGTH = 50;

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Long id;

    @Getter
    @Column(name = "first_name", nullable = false,  length = MAX_NAME_LENGTH)
    private String firstName;

    @Getter
    @Column(name = "last_name", nullable = false, length = MAX_NAME_LENGTH)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true, length = MAX_USERNAME_LENGTH)
    private String username;

    @Getter
    @Column(unique = true, name = "email", length = MAX_EMAIL_LENGTH)
    @Nullable
    private String email;

    @Getter
    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Getter
    @Column(name = "role", nullable = false)
    private String role;

    @Nullable
    @Getter
    @Column(name = "reserved_email")
    private String reservedEmail;

    @Getter
    @Column(name = "verified", nullable = false)
    private boolean verified;

    @Nullable
    @Getter
    @Column(name = "verification_code", unique = true, length = MAX_VERIFICATION_CODE_LENGTH)
    private String verificationCode;

    @Nullable
    @Getter
    @Column(name = "verification_code_expiration")
    private Instant verificationCodeExpiration;

    @Nullable
    @Getter
    @Column(name = "password_change_code", unique = true, length = MAX_CHANGE_PASSWORD_CODE_LENGTH)
    private String passwordChangeCode;

    @Nullable
    @Getter
    @Column(name = "password_change_code_expiration")
    private Instant passwordChangeCodeExpiration;

    @Nullable
    @Getter
    @Column(name = "email_change_code", unique = true, length = MAX_EMAIL_CHANGE_CODE_LENGTH)
    private String emailChangeCode;

    @Nullable
    @Getter
    @Column(name = "email_change_code_expiration")
    private Instant emailChangeCodeExpiration;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @Getter
    @Nullable
    @OneToOne(cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    @JoinColumn(name = "split_id")
    private Split split;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Exercise> exercises;

    public User(String firstName, String lastName, String username, String email, String hashedPassword, String verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.reservedEmail = email;
        this.hashedPassword = hashedPassword;
        this.verified = false;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiration = Instant.now().plus(REGISTRATION_EXPIRATION);
        this.passwordChangeCode = null;
        this.passwordChangeCodeExpiration = null;
        this.emailChangeCode = null;
        this.emailChangeCodeExpiration = null;
        this.role = "ROLE_" + ROLE_USER;
        this.enabled = true;
        this.locked = false;

        this.exercises = new HashSet<>();
        this.split = null;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    public void verify(String sentVerificationCode, Instant now) throws VerificationException {
        if (verified) {
            throw new VerificationException("User with ID: %s is already verified!".formatted(this.id));
        }

        assert this.verificationCode != null;
        if (!this.verificationCode.equals(sentVerificationCode)) {
            throw new VerificationException("The given verification code does not match the one of the user!");
        }

        assert this.verificationCodeExpiration != null;
        if (this.verificationCodeExpiration.isBefore(now)) {
            throw new VerificationException("The given verification code is already expired. Please apply for a renewal!");
        }

        assert this.reservedEmail != null;
        this.email = this.reservedEmail;
        this.reservedEmail = null;
        this.verificationCode = null;
        this.verificationCodeExpiration = null;

        this.verified = true;
    }

    public void requestPasswordChange(String passwordChangeCode) {
        this.passwordChangeCode = passwordChangeCode;
        this.passwordChangeCodeExpiration = Instant.now().plus(CHANGE_PASSWORD_DURATION);
    }

    public void verifyPasswordChange(String password, String passwordChangeCode, Instant now) throws VerificationException {
        assert this.passwordChangeCode != null;
        if (!this.passwordChangeCode.equals(passwordChangeCode)) {
            throw new VerificationException("The given password change code does not match the one of the user!");
        }

        assert this.passwordChangeCodeExpiration != null;
        if (this.passwordChangeCodeExpiration.isBefore(now)) {
            throw new VerificationException("The given password change code has expired! Please apply for a renewal!");
        }

        this.hashedPassword = password;
        this.passwordChangeCode = null;
        this.passwordChangeCodeExpiration = null;
    }

    public void changeUsername(String username) {
        this.username = username;
    }

    public void changePassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void requestEmailChange(String reservedEmail, String emailChangeCode) {
        this.emailChangeCode = emailChangeCode;
        this.emailChangeCodeExpiration = Instant.now().plus(CHANGE_EMAIL_DURATION);
        this.reservedEmail = reservedEmail;
    }

    public void confirmEmailChange(String emailChangeCode, Instant now) throws VerificationException {
        assert this.emailChangeCode != null;
        if (!this.emailChangeCode.equals(emailChangeCode)) {
            throw new VerificationException("The given email change code does not match the one of the user!");
        }

        assert this.emailChangeCodeExpiration != null;
        if (this.emailChangeCodeExpiration.isBefore(now)) {
            throw new VerificationException("The given email change code has expired! Please apply for a renewal!");
        }

        if (!this.verified) { // should actually never happen but just to be sure!
            throw new VerificationException("The user is not yet verified and must not change his email until he is!");
        }

        this.email = this.reservedEmail;
        this.reservedEmail = null;
        this.emailChangeCode = null;
        this.emailChangeCodeExpiration = null;
    }

    public void changeName(@Nullable String newFirstName, @Nullable String newLastName) {
        if (newFirstName != null) {
            this.firstName = newFirstName;
        }

        if (newLastName != null) {
            this.lastName = newLastName;
        }
    }

    public Exercise addExercise(String name) {
        assert this.exercises.stream().noneMatch(exercise -> exercise.getName().equalsIgnoreCase(name));

        Exercise exercise = new Exercise(this, name);
        this.exercises.add(exercise);
        return exercise;
    }

    public Split changeSplit(String name, Set<String> dayNames) {
        Split split = new Split(name, dayNames);
        this.split = split;
        return split;
    }
}

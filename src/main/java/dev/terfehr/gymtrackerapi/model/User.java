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
import java.util.List;

@Entity
@Table(name = "users")
@NullMarked
@NoArgsConstructor
public class User implements UserDetails {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
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
    @Column(name = "reserved_email", unique = true)
    private String reservedEmail;

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

    public User(String firstName, String lastName, String username, String email, String hashedPassword, String verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.reservedEmail = email;
        this.hashedPassword = hashedPassword;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiration = Instant.now().plus(REGISTRATION_EXPIRATION);
        this.passwordChangeCode = null;
        this.passwordChangeCodeExpiration = null;
        this.emailChangeCode = null;
        this.emailChangeCodeExpiration = null;
        this.role = ROLE_USER;
        this.enabled = false;
        this.locked = false;
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

    public void enable(String sentVerificationCode, Instant now) throws VerificationException {
        if (enabled) {
            throw new VerificationException("User with ID: %s is already enabled!".formatted(this.id));
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

        this.enabled = true;
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
        assert this.reservedEmail == null; // only gets called if user is enabled already
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
        if (!this.emailChangeCodeExpiration.isBefore(now)) {
            throw new VerificationException("The given email change code has expired! Please apply for a renewal!");
        }

        if (!this.enabled) {
            throw new VerificationException("The user is not yet verified and must not change his email until he is!");
        }

        this.email = this.reservedEmail;
        this.reservedEmail = null;
        this.emailChangeCode = null;
        this.emailChangeCodeExpiration = null;
    }
}

package dev.terfehr.gymtrackerapi.model;

import dev.terfehr.gymtrackerapi.exception.VerificationException;
import jakarta.persistence.*;
import lombok.Getter;
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
public class User implements UserDetails {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final Duration REGISTRATION_EXPIRATION = Duration.ofDays(1);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Long id;

    @Getter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Getter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Getter
    @Column(unique = true, name = "email")
    @Nullable
    private String email;

    @Getter
    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "role", nullable = false)
    private String role;


    @Nullable
    @Getter
    @Column(name = "reserved_email", unique = true)
    private String reservedEmail;

    @Nullable
    @Getter
    @Column(name = "verification_code", unique = true)
    private String verificationCode;

    @Nullable
    @Getter
    @Column(name = "verification_code_expiration")
    private Instant verificationCodeExpiration;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    public User() {}

    public User(String firstName, String lastName, String email, String hashedPassword, String verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.reservedEmail = email;
        this.hashedPassword = hashedPassword;
        this.verificationCode = verificationCode;
        this.role = ROLE_USER;
        this.verificationCodeExpiration = Instant.now().plus(REGISTRATION_EXPIRATION);
        this.enabled = false;
        this.locked = false;
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    /**
     * @return The Email as a String since we do not use Usernames
     */
    @Override
    public String getUsername() {
        return this.email;
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
}

package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.LoginDTO;
import dev.terfehr.gymtrackerapi.dto.RefreshAccessTokenDTO;
import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.exception.*;
import dev.terfehr.gymtrackerapi.infrastructure.EmailServiceI;
import dev.terfehr.gymtrackerapi.infrastructure.UUIDService;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import dev.terfehr.gymtrackerapi.security.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@NullMarked
@Service
@AllArgsConstructor
public class AuthService {


    private final EmailServiceI emailService;
    private final UserRepositoryI userRepository;
    private final UUIDService uuidService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDTO registerUser(String firstName, String lastName, String username, String email, String password, String password2) throws CredentialsTakenException {
        if (userRepository.existsByEmail(email)) {
            throw new CredentialsTakenException("The email address " +  email + " is already taken");
        }

        if (userRepository.existsByUsername(username)) {
            throw new CredentialsTakenException("The username " +  username + " is already taken");
        }

        if (!password.equals(password2)) {
            throw new AuthenticationException("Given passwords do not match! Please send another registration request");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String verificationCode = uuidService.generateUniqueVerificationCode();

        assert encodedPassword != null;
        User user = new User(firstName, lastName, username, email, encodedPassword, verificationCode);
        userRepository.save(user);

        emailService.sendRegistrationEmail(email, verificationCode);

        return new UserDTO(user);
    }

    public UserDTO verifyUser(String verificationCode) throws ResourceNotFoundException, VerificationException, CredentialsTakenException {
        User user = userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with the verification code " + verificationCode));

        String reservedEmail = user.getReservedEmail();
        assert  reservedEmail != null;
        boolean emailTaken = userRepository.existsByEmail(reservedEmail);

        if (emailTaken) {
            throw new CredentialsTakenException("There already is a user with the email " +
                    reservedEmail);
        }

        user.enable(verificationCode, Instant.now());

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail());

        return new UserDTO(user);
    }

    public LoginDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with the username " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("The given password does not match the users password");
        }

        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(user);

        return new LoginDTO(user, refreshToken, accessToken);
    }

    public RefreshAccessTokenDTO refreshAccessToken(String refreshToken) throws ExpiredJwtException, AuthenticationException {
        String username = jwtService.extractUsername(refreshToken); // throws ExpiredJwtException, if token has ran out

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("There is no user represented by the sent refresh token! Please consider logging in again"));

        boolean valid = jwtService.isRefreshTokenValid(refreshToken, user);

        if (!valid)
            throw new AuthenticationException("The refresh token is not valid");

        String accessToken = jwtService.generateAccessToken(user);

        return new RefreshAccessTokenDTO(user, accessToken);
    }

    public String requestPasswordReset(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        Optional<User> userByReservedEmail =  userRepository.findByReservedEmail(email);
        String message = "A mail has been sent to the user with the given email address if it exists! It contains a link to reset the password";

        boolean noUserByEmail = userByEmail.isEmpty();

        if (noUserByEmail && userByReservedEmail.isEmpty()) {
            return message;
        }

        User user = noUserByEmail ? userByReservedEmail.get() : userByEmail.get();

        String passwordChangeCode = uuidService.generateUniquePasswordChangeCode();

        user.requestPasswordChange(passwordChangeCode);

        userRepository.save(user);

        emailService.sendRequestPasswordChangeEmail(email, passwordChangeCode);

        return message;
    }

    public void confirmPasswordReset(String passwordChangeCode, String password, String password2) {
        if (!password.equals(password2)) {
            throw new  AuthenticationException("Given passwords do not match! Please send another request");
        }

        User user = userRepository.findByPasswordChangeCode(passwordChangeCode)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with the password change code " + passwordChangeCode));

        String encodedPassword = passwordEncoder.encode(password);

        assert encodedPassword != null;
        user.verifyPasswordChange(encodedPassword, passwordChangeCode, Instant.now());
    }
}

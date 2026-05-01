package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.LoginDTO;
import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.exception.AuthenticationException;
import dev.terfehr.gymtrackerapi.exception.CredentialsTakenException;
import dev.terfehr.gymtrackerapi.exception.ResourceNotFoundException;
import dev.terfehr.gymtrackerapi.exception.VerificationException;
import dev.terfehr.gymtrackerapi.infrastructure.EmailServiceI;
import dev.terfehr.gymtrackerapi.infrastructure.UUIDService;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import dev.terfehr.gymtrackerapi.security.JwtService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@NullMarked
@Service
@AllArgsConstructor
public class AuthService {


    private final EmailServiceI emailService;
    private final UserRepositoryI userRepository;
    private final UUIDService uuidService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserDTO registerUser(String firstName, String lastName, String username, String email, String password) throws CredentialsTakenException {
        if (userRepository.existsByEmail(email)) {
            throw new CredentialsTakenException("The email address " +  email + " is already taken");
        }

        if (userRepository.existsByUsername(username)) {
            throw new CredentialsTakenException("The username " +  username + " is already taken");
        }

        String encodedPassword = passwordEncoder.encode(password);
        String verificationCode = uuidService.generateUniqueVerificationCode();

        assert encodedPassword != null;
        User user = new User(firstName, lastName, username, email, encodedPassword, verificationCode);
        userRepository.save(user);

        emailService.sendRegistrationEmail(email, verificationCode);

        return new UserDTO(user);
    }

    public UserDTO verifyUser(String verificationCode) throws ResourceNotFoundException, VerificationException {
        User user = userRepository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with the verification code " + verificationCode));

        user.enable(verificationCode, Instant.now());

        userRepository.save(user);

        emailService.sendVerificationEmail(user.getEmail());

        return new UserDTO(user);
    }

    public LoginDTO login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("There is no user with the username " + username));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("The given password does not match the users password")
        }

        String refreshToken = jwtService.generateRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(user);

        return new LoginDTO(user, refreshToken, accessToken);
    }
}

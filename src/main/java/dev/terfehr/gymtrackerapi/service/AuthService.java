package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.exception.CredentialsTakenException;
import dev.terfehr.gymtrackerapi.infrastructure.EmailService;
import dev.terfehr.gymtrackerapi.infrastructure.UUIDService;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@NullMarked
@Service
@AllArgsConstructor
public class AuthService {


    private final EmailService emailService;
    private final UserRepositoryI userRepository;
    private final UUIDService uuidService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO registerUser(String firstName, String lastName, String username, String email, String password) {
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
}

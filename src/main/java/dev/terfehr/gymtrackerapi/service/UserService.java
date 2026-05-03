package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.UserDTO;
import dev.terfehr.gymtrackerapi.exception.AuthenticationException;
import dev.terfehr.gymtrackerapi.exception.CredentialsTakenException;
import dev.terfehr.gymtrackerapi.infrastructure.EmailServiceI;
import dev.terfehr.gymtrackerapi.infrastructure.UUIDService;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@NullMarked
@Service
@AllArgsConstructor
@Transactional
public class UserService {

    private final EmailServiceI emailService;
    private final UserRepositoryI userRepository;
    private final UUIDService uuidService;
    private final PasswordEncoder passwordEncoder;

    public UserDTO changeUsername(User authUser, String username, String password) throws AuthenticationException, CredentialsTakenException {
        if (!passwordEncoder.matches(password, authUser.getPassword())) {
            throw new AuthenticationException("The given password is incorrect");
        }

        if (userRepository.existsByUsername(username)) {
            throw new CredentialsTakenException("The username " + username + " is already in use");
        }

        authUser.changeUsername(username);
        userRepository.save(authUser);

        return new UserDTO(authUser);
    }

    public UserDTO changePassword(User authUser, String oldPassword, String password, String password2) throws AuthenticationException {
        if (!password.equals(password2)) {
            throw new AuthenticationException("The passwords do not match");
        }

        if (!passwordEncoder.matches(oldPassword, authUser.getPassword())) {
            throw new AuthenticationException("The provided password does not match the current users' one");
        }

        String hashedPassword = passwordEncoder.encode(password);

        assert hashedPassword != null;
        authUser.changePassword(hashedPassword);
        userRepository.save(authUser);

        return new UserDTO(authUser);
    }

    @PreAuthorize("principal.enabled")
    public UserDTO requestEmailChange(User authUser, String currentPassword, String desiredEmail) throws AuthenticationException, CredentialsTakenException {
        if (!passwordEncoder.matches(currentPassword, authUser.getPassword())) {
            throw new AuthenticationException("The provided password does not match the current users' one");
        }

        if (userRepository.existsByEmail(desiredEmail)) {
            throw new CredentialsTakenException("The email " + desiredEmail + " is already in use");
        }

        String changeEmailCode = uuidService.generateUniqueVerificationCode();
        authUser.requestEmailChange(desiredEmail, changeEmailCode);
        userRepository.save(authUser);

        emailService.sendRequestEmailChangeEmail(desiredEmail, changeEmailCode);

        return new UserDTO(authUser);
    }
}

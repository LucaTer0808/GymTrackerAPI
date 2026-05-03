package dev.terfehr.gymtrackerapi.infrastructure;

import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.util.UUID;

@NullMarked
@Service
@AllArgsConstructor
public class UUIDService {

    private final UserRepositoryI userRepository;

    public String generateUniqueVerificationCode() {
        String code;
        do {
            code = UUID.randomUUID().toString();
        } while (!userRepository.existsByVerificationCode(code));

        return code;
    }

    public String generateUniquePasswordChangeCode() {
        String code;
        do {
            code = UUID.randomUUID().toString();
        } while(!userRepository.existsByPasswordChangeCode(code));

        return code;
    }

    public String generateUniqueEmailChangeCode() {
        String code;
        do {
            code = UUID.randomUUID().toString();
        } while(!userRepository.existsByEmailChangeCode(code));

        return code;
    }
}

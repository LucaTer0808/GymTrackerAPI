package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.User;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NullMarked
public interface UserRepositoryI extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    Optional<User> findByReservedEmail(String reservedEmail);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByVerificationCode(String verificationCode);
}

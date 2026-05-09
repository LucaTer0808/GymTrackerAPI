package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExecutionRepositoryI extends JpaRepository<Execution, Long> {
    Optional<Execution> findByIdAndExerciseUserId(Long id, Long userId);
}

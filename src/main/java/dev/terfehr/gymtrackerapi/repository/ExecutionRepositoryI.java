package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.Execution;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

public interface ExecutionRepositoryI extends JpaRepository<Execution, Long> {
    Optional<Execution> findByIdAndExerciseUserId(Long id, Long userId);
}

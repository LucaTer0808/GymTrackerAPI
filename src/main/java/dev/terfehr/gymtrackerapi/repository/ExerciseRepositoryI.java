package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseRepositoryI extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByIdAndUserId(Long id, Long userId);
    List<Exercise> findAllByUserIdAndIdIn(Long userId, List<Long> ids);
    boolean existsByUserIdAndName(Long userId, String name);
}

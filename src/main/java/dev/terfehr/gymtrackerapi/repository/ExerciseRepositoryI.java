package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepositoryI extends JpaRepository<Exercise, Long> {
    boolean existsByUserIdAndName(Long userId, String name);
}

package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.dto.ExerciseDateTuple;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ExerciseRepositoryI extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByIdAndUserId(Long id, Long userId);
    List<Exercise> findAllByUserIdAndIdIn(Long userId, List<Long> ids);
    boolean existsByUserIdAndName(Long userId, String name);

    @Query("""
        SELECT new dev.terfehr.gymtrackerapi.dto.ExerciseDateTuple(ex, MAX(ec.dateTime))
        FROM Exercise ex
        LEFT JOIN Execution ec ON ec.exercise = ex
        WHERE ex.user = :user
        GROUP BY ex.id
    """)
    Set<ExerciseDateTuple> findExercisesWithLatestDateOfExecutionByUser(@Param("user") User user);
}

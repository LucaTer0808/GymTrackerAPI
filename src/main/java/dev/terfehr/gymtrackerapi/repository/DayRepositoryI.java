package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DayRepositoryI extends JpaRepository<Day, Long> {
    Optional<Day> findByIdAndSplitUserId(Long id, Long userId);
    boolean existsBySplitUserIdAndName(Long userId, String name);
}

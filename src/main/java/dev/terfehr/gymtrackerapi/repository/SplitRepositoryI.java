package dev.terfehr.gymtrackerapi.repository;

import dev.terfehr.gymtrackerapi.model.Split;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@NullMarked
@Repository
public interface SplitRepositoryI extends JpaRepository<Split, Long> {

    Long getIdByUserId(Long userId);
}

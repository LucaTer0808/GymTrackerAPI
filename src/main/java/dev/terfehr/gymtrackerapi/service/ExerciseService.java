package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.ExerciseDTO;
import dev.terfehr.gymtrackerapi.exception.DatabaseConflictException;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.ExerciseRepositoryI;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

@Service
@NullMarked
@AllArgsConstructor
public class ExerciseService {

    private final ExerciseRepositoryI exerciseRepository;
    private final UserRepositoryI userRepositoryI;

    @Transactional
    public ExerciseDTO createExercise(User authUser, String name) throws DatabaseConflictException {
        if (exerciseRepository.existsByUserIdAndName(authUser.getId(), name)) {
            throw new DatabaseConflictException("There already is an exercise named " + name + " for the user with ID " + authUser.getId());
        }

        Exercise exercise = authUser.addExercise(name);
        userRepositoryI.save(authUser);

        return new ExerciseDTO(exercise);
    }
}

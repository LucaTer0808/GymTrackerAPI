package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.ExerciseDTO;
import dev.terfehr.gymtrackerapi.dto.ExerciseWithoutExecutionDTO;
import dev.terfehr.gymtrackerapi.exception.DatabaseConflictException;
import dev.terfehr.gymtrackerapi.exception.ResourceNotFoundException;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.ExerciseRepositoryI;
import dev.terfehr.gymtrackerapi.repository.UserRepositoryI;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

@Service
@NullMarked
@AllArgsConstructor
@Transactional
public class ExerciseService {

    private final ExerciseRepositoryI exerciseRepository;
    private final UserRepositoryI userRepository;

    public ExerciseWithoutExecutionDTO createExercise(User authUser, String name) throws DatabaseConflictException {
        if (exerciseRepository.existsByUserIdAndName(authUser.getId(), name)) {
            throw new DatabaseConflictException("There already is an exercise named " + name + " for the user with ID " + authUser.getId());
        }

        Exercise exercise = authUser.addExercise(name);
        userRepository.save(authUser);

        return new ExerciseWithoutExecutionDTO(exercise);
    }

    public ExerciseDTO getExercise(User authUser, long exerciseId) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert authUserId != null;

        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with ID " + exerciseId + " not found"));

        return new ExerciseDTO(exercise);
    }

    public ExerciseWithoutExecutionDTO updateExercise(User authUser, long exerciseId, @Nullable String name) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert authUserId != null;

        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with ID " + exerciseId + " not found"));

        if (name != null) {
            exercise.changeName(name);
        }

        exerciseRepository.save(exercise);
        return new ExerciseWithoutExecutionDTO(exercise);
    }

    public void deleteExercise(User authUser, long exerciseId) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert authUserId != null;

        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise with ID " + exerciseId + " not found"));

        authUser.deleteExercise(exercise);

        exerciseRepository.delete(exercise);
        userRepository.save(authUser);
    }
}

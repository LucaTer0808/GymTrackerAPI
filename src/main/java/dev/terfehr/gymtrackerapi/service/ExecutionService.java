package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.ExecutionDTO;
import dev.terfehr.gymtrackerapi.dto.request.ExecutionSetRequestDTO;
import dev.terfehr.gymtrackerapi.exception.ResourceNotFoundException;
import dev.terfehr.gymtrackerapi.model.Execution;
import dev.terfehr.gymtrackerapi.model.ExecutionSet;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.User;
import dev.terfehr.gymtrackerapi.repository.ExecutionRepositoryI;
import dev.terfehr.gymtrackerapi.repository.ExerciseRepositoryI;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@NullMarked
@Transactional
@AllArgsConstructor
public class ExecutionService {

    private final ExecutionRepositoryI executionRepository;
    private final ExerciseRepositoryI exerciseRepository;

    @PreAuthorize("authorizationService.isVerified(principal)")
    public ExecutionDTO createExecution(User authUser, long exerciseId, List<ExecutionSetRequestDTO> executionSetDTOs) {
        Long authUserId = authUser.getId();
        assert  authUserId != null;

        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("The exercise with id: " + exerciseId + " does not exist"));

        Execution execution = exercise.addEmptyExecution();

        List<ExecutionSet> executionSets = createExecutionSets(execution, executionSetDTOs);
        execution.setExecutionSets(executionSets);

        exerciseRepository.save(exercise); // due to cascadeType.ALL, the execution is saved as well

        return new ExecutionDTO(execution);
    }

    @PreAuthorize("authorizationService.isVerified(principal)")
    public List<ExecutionDTO> getExecutions(User authUser, long exerciseId) {
        Long authUserId = authUser.getId();
        assert  authUserId != null;

        Exercise exercise = exerciseRepository.findByIdAndUserId(exerciseId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("The exercise with id: " + exerciseId + " does not exist"));

        List<ExecutionDTO> executionDTOs = new ArrayList<>();

        for (Execution execution : exercise.getExecutions()) {
            executionDTOs.add(new ExecutionDTO(execution));
        }

        return executionDTOs;
    }

    @PreAuthorize("authorizationService.isVerified(principal)")
    public ExecutionDTO updateExecution(User authUser, long executionId,
                                        @Nullable LocalDate date,
                                        @Nullable List<ExecutionSetRequestDTO> executionSetDTOs) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert  authUserId != null;

        Execution execution = executionRepository.findByIdAndExerciseUserId(executionId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("The execution with id: " + executionId + " does not exist"));

        if (date != null) {
            execution.changeDate(date);
        }

        if (executionSetDTOs != null) {
            List<ExecutionSet> executionSets = createExecutionSets(execution, executionSetDTOs);
            execution.setExecutionSets(executionSets);
        }

        executionRepository.save(execution);
        return new ExecutionDTO(execution);
    }

    @PreAuthorize("authorizationService.isVerified(principal)")
    public void deleteExecution(User authUser, long executionId) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert  authUserId != null;

        Execution execution = executionRepository.findByIdAndExerciseUserId(executionId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("The execution with id: " + executionId + " does not exist"));

        Exercise exercise = execution.getExercise();
        exercise.deleteExecution(execution);

        exerciseRepository.save(exercise);
    }

    private List<ExecutionSet> createExecutionSets(Execution execution, List<ExecutionSetRequestDTO> executionSetDTOs) {
        List<ExecutionSet> executionSets = new ArrayList<>();

        for (ExecutionSetRequestDTO dto : executionSetDTOs) {
            executionSets.add(new ExecutionSet(execution, dto.numberInExecution(), dto.weight(), dto.reps()));
        }

        return executionSets;
    }
}

package dev.terfehr.gymtrackerapi.service;

import dev.terfehr.gymtrackerapi.dto.DayDTO;
import dev.terfehr.gymtrackerapi.exception.DatabaseConflictException;
import dev.terfehr.gymtrackerapi.exception.ResourceNotFoundException;
import dev.terfehr.gymtrackerapi.model.*;
import dev.terfehr.gymtrackerapi.repository.DayRepositoryI;
import dev.terfehr.gymtrackerapi.repository.ExerciseRepositoryI;
import dev.terfehr.gymtrackerapi.repository.SplitRepositoryI;
import dev.terfehr.gymtrackerapi.utils.AssertionUtils;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@NullMarked
@Transactional
public class DayService {

    private final DayRepositoryI dayRepository;
    private final ExerciseRepositoryI exerciseRepository;
    private final SplitRepositoryI splitRepository;

    public DayDTO createDay(User authUser, String name, List<Long> exerciseIds) throws DatabaseConflictException, ResourceNotFoundException {
        Split split = authUser.getSplit();

        if (split == null) {
            throw new ResourceNotFoundException("Authenticated User currently has no split. No day can be added");
        }

        Long authUserId = authUser.getId();
        assert authUserId != null; // comes from the db, cannot be null

        if (dayRepository.existsBySplitUserIdAndName(authUserId, name)) {
            throw new DatabaseConflictException("There already is a day with the name " + name + " for the user with ID " + authUser.getId());
        }

        Day day = split.addDay(name);
        splitRepository.save(split);

        createNewExerciseSlots(day, authUserId, exerciseIds);

        return new DayDTO(day);
    }

    public DayDTO getDay(User authUser, long dayId) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert authUserId != null;

        Day day = dayRepository.findByIdAndSplitUserId(dayId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Day with ID " + dayId + " does not exist"));

        return new DayDTO(day);
    }

    public DayDTO updateDay(User authUser, long dayId, @Nullable String name, @Nullable List<Long> exerciseIds) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert authUserId != null;

        Day day = dayRepository.findByIdAndSplitUserId(dayId,  authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Day with ID " + dayId + " does not exist"));

        if (name != null && !name.equals(day.getName())) {
            if (dayRepository.existsBySplitUserIdAndName(authUserId, name)) {
                throw new DatabaseConflictException("There already is a day with the name " + name + " for the user with ID " + authUser.getId());
            }

            day.changeName(name);
        }

        if (exerciseIds != null) {
            deleteOldExerciseSlots(day);
            createNewExerciseSlots(day, authUserId, exerciseIds);
        }

        return new DayDTO(day);
    }

    public void deleteDay(User authUser, long dayId) throws ResourceNotFoundException {
        Long authUserId = authUser.getId();
        assert authUserId != null;

        Day day = dayRepository.findByIdAndSplitUserId(dayId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Day with ID " + dayId + " does not exist"));

        deleteOldExerciseSlots(day);

        Split split = day.getSplit();
        split.removeDay(day);

        dayRepository.delete(day);
        splitRepository.save(split);
    }

    private void deleteOldExerciseSlots(Day day) {
        Set<ExerciseSlot> oldSlots = new HashSet<>(day.getExerciseSlots());
        Set<Exercise> exercisesToSave = new HashSet<>();

        for (ExerciseSlot exerciseSlot : oldSlots) {
            Exercise exerciseToSlot =  exerciseSlot.getExercise();

            day.removeExerciseSlot(exerciseSlot);
            exerciseToSlot.removeExerciseSlot(exerciseSlot);

            exercisesToSave.add(exerciseToSlot);
        }

        dayRepository.save(day);
        exerciseRepository.saveAll(exercisesToSave);
    }

    private void createNewExerciseSlots(Day day, Long authUserId, List<Long> exerciseIds) {
        assert AssertionUtils.doesNotContainDuplicates(exerciseIds);

        List<Exercise> exercises = exerciseRepository.findAllByUserIdAndIdIn(authUserId, exerciseIds);
        if (exercises.size() != exerciseIds.size()) {
            throw new ResourceNotFoundException("Not all exercises have been found. Please provide a list of only existing Exercises");
        }

        for (Exercise exercise : exercises) {
            ExerciseSlot slot = new ExerciseSlot(day, exercise);
            day.addExerciseSlot(slot);
            exercise.addExerciseSlot(slot);
        }

        dayRepository.save(day);
        exerciseRepository.saveAll(exercises);
    }
}

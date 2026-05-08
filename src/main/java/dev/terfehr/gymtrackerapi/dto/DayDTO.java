package dev.terfehr.gymtrackerapi.dto;

import dev.terfehr.gymtrackerapi.model.Day;
import dev.terfehr.gymtrackerapi.model.ExerciseSlot;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public record DayDTO(long id, String name, List<ExerciseWithoutExecutionDTO> exercises) {
    public DayDTO(Day day) {
        Long id = day.getId();
        assert  id != null;

        List<ExerciseWithoutExecutionDTO> exerciseWithoutExecutionDTOs = new ArrayList<>();

        for (ExerciseSlot exerciseSlot : day.getExerciseSlots()) {
            exerciseWithoutExecutionDTOs.add(new ExerciseWithoutExecutionDTO(exerciseSlot.getExercise()));
        }

        this(id, day.getName(), exerciseWithoutExecutionDTOs);
    }
}

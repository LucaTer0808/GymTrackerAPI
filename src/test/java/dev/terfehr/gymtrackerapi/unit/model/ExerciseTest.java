package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseTest {

    private User createUser() {
        return new User(
                "Max",
                "Mustermann",
                "max12345",
                "max@mail.com",
                "hashedPw",
                "verifyCode"
        );
    }

    private Exercise createExercise() {
        return new Exercise(createUser(), "Bench Press");
    }

    private Day createDay() {
        User user = createUser();
        Split split = new Split(user, "Push Pull", List.of("Push"));
        return new Day(split, "Push");
    }

    private ExerciseSlot createSlot() {
        return new ExerciseSlot(createDay(), createExercise());
    }

    @Test
    void shouldCreateExerciseCorrectly() {
        Exercise exercise = createExercise();

        assertEquals("Bench Press", exercise.getName());
        assertNotNull(exercise.getUser());
        assertNotNull(exercise.getExerciseSlots());
        assertNotNull(exercise.getExecutions());
        assertTrue(exercise.getExerciseSlots().isEmpty());
        assertTrue(exercise.getExecutions().isEmpty());
    }

    @Test
    void shouldChangeName() {
        Exercise exercise = createExercise();

        exercise.changeName("Squat");

        assertEquals("Squat", exercise.getName());
    }

    @Test
    void shouldAddExecution() {
        Exercise exercise = createExercise();

        Execution execution = exercise.addEmptyExecution();

        assertNotNull(execution);
        assertEquals(1, exercise.getExecutions().size());
    }

    @Test
    void shouldDeleteExecution() {
        Exercise exercise = createExercise();

        Execution execution = exercise.addEmptyExecution();

        exercise.deleteExecution(execution);

        assertEquals(0, exercise.getExecutions().size());
    }

    @Test
    void shouldFailOnDeletingNonExistingExecution() {
        Exercise exercise = createExercise();
        Execution fake = new Execution(exercise);

        assertThrows(AssertionError.class, () ->
                exercise.deleteExecution(fake)
        );
    }

    @Test
    void shouldAddExerciseSlot() {
        Exercise exercise = createExercise();
        ExerciseSlot slot = createSlot();

        exercise.addExerciseSlot(slot);

        assertTrue(exercise.getExerciseSlots().contains(slot));
        assertEquals(1, exercise.getExerciseSlots().size());
    }

    @Test
    void shouldRemoveExerciseSlot() {
        Exercise exercise = createExercise();
        ExerciseSlot slot = createSlot();

        exercise.addExerciseSlot(slot);
        exercise.removeExerciseSlot(slot);

        assertFalse(exercise.getExerciseSlots().contains(slot));
    }

    @Test
    void shouldFailOnRemovingNonExistingSlot() {
        Exercise exercise = createExercise();
        ExerciseSlot slot = createSlot();

        assertThrows(AssertionError.class, () ->
                exercise.removeExerciseSlot(slot)
        );
    }

    @Test
    void shouldMaintainMultipleSlots() {
        Exercise exercise = createExercise();

        ExerciseSlot slot1 = createSlot();
        ExerciseSlot slot2 = createSlot();

        exercise.addExerciseSlot(slot1);
        exercise.addExerciseSlot(slot2);

        assertEquals(2, exercise.getExerciseSlots().size());
    }
}
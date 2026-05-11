package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DayTest {

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

    private Split createSplit() {
        return new Split(
                createUser(),
                "Push Pull Legs",
                List.of("Push", "Pull", "Legs")
        );
    }

    private Day createDay() {
        return new Day(createSplit(), "Push");
    }

    private ExerciseSlot createSlot() {
        return new ExerciseSlot(createDay(), new Exercise(createUser(), "Bench Press"));
    }

    @Test
    void shouldCreateDayCorrectly() {
        Day day = createDay();

        assertEquals("Push", day.getName());
        assertNotNull(day.getSplit());
        assertNotNull(day.getExerciseSlots());
        assertTrue(day.getExerciseSlots().isEmpty());
    }

    @Test
    void shouldChangeName() {
        Day day = createDay();

        day.changeName("Leg Day");

        assertEquals("Leg Day", day.getName());
    }

    @Test
    void shouldAddExerciseSlot() {
        Day day = createDay();
        ExerciseSlot slot = createSlot();

        day.addExerciseSlot(slot);

        assertEquals(1, day.getExerciseSlots().size());
        assertTrue(day.getExerciseSlots().contains(slot));
    }

    @Test
    void shouldRemoveExerciseSlot() {
        Day day = createDay();
        ExerciseSlot slot = createSlot();

        day.addExerciseSlot(slot);
        day.removeExerciseSlot(slot);

        assertFalse(day.getExerciseSlots().contains(slot));
        assertTrue(day.getExerciseSlots().isEmpty());
    }

    @Test
    void shouldMaintainMultipleSlots() {
        Day day = createDay();

        ExerciseSlot slot1 = createSlot();
        ExerciseSlot slot2 = createSlot();

        day.addExerciseSlot(slot1);
        day.addExerciseSlot(slot2);

        assertEquals(2, day.getExerciseSlots().size());
    }

    @Test
    void shouldFailWhenAddingDuplicateSlot() {
        Day day = createDay();
        ExerciseSlot slot = createSlot();

        day.addExerciseSlot(slot);

        assertThrows(AssertionError.class, () ->
                day.addExerciseSlot(slot)
        );
    }

    @Test
    void shouldFailWhenRemovingNonExistingSlot() {
        Day day = createDay();
        ExerciseSlot slot = createSlot();

        assertThrows(AssertionError.class, () ->
                day.removeExerciseSlot(slot)
        );
    }

    @Test
    void shouldBeLinkedToSplit() {
        Split split = createSplit();
        Day day = new Day(split, "Push");

        assertSame(split, day.getSplit());
    }
}
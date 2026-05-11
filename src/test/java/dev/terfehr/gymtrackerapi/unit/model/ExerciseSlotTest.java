package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.model.Day;
import dev.terfehr.gymtrackerapi.model.Exercise;
import dev.terfehr.gymtrackerapi.model.ExerciseSlot;
import dev.terfehr.gymtrackerapi.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseSlotTest {

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

    private Day createDay() {
        User user = createUser();
        return new Day(
                new dev.terfehr.gymtrackerapi.model.Split(
                        user,
                        "Split",
                        java.util.List.of("Push")
                ),
                "Push"
        );
    }

    private Exercise createExercise() {
        User user = createUser();
        return new Exercise(user, "Bench Press");
    }

    @Test
    void shouldCreateExerciseSlotCorrectly() {
        Day day = createDay();
        Exercise exercise = createExercise();

        ExerciseSlot slot = new ExerciseSlot(day, exercise);

        assertNotNull(slot);
        assertEquals(day, slot.getDay());
        assertEquals(exercise, slot.getExercise());
    }

    @Test
    void shouldBindDayAndExerciseTogether() {
        Day day = createDay();
        Exercise exercise = createExercise();

        ExerciseSlot slot = new ExerciseSlot(day, exercise);

        assertSame(day, slot.getDay());
        assertSame(exercise, slot.getExercise());
    }

    @Test
    void shouldAllowMultipleSlotsWithSameExerciseDifferentDay() {
        Day day1 = createDay();
        Day day2 = createDay();
        Exercise exercise = createExercise();

        ExerciseSlot slot1 = new ExerciseSlot(day1, exercise);
        ExerciseSlot slot2 = new ExerciseSlot(day2, exercise);

        assertNotEquals(slot1.getDay(), slot2.getDay());
        assertEquals(slot1.getExercise(), slot2.getExercise());
    }

    @Test
    void shouldAllowMultipleSlotsWithSameDayDifferentExercises() {
        Day day = createDay();

        Exercise ex1 = createExercise();
        Exercise ex2 = createExercise();

        ExerciseSlot slot1 = new ExerciseSlot(day, ex1);
        ExerciseSlot slot2 = new ExerciseSlot(day, ex2);

        assertEquals(slot1.getDay(), slot2.getDay());
        assertNotEquals(slot1.getExercise(), slot2.getExercise());
    }

    @Test
    void shouldNotBeNullAfterCreation() {
        ExerciseSlot slot = new ExerciseSlot(createDay(), createExercise());

        assertNotNull(slot.getDay());
        assertNotNull(slot.getExercise());
    }
}
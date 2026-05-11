package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionTest {

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

    private Execution createExecution() {
        return new Execution(createExercise());
    }

    @Test
    void shouldCreateExecutionCorrectly() {
        Execution execution = createExecution();

        assertNotNull(execution.getExercise());
        assertNotNull(execution.getDate());
        assertNotNull(execution.getExecutionSets());

        assertTrue(execution.getExecutionSets().isEmpty());
    }

    @Test
    void shouldSetDateToTodayOnCreation() {
        Execution execution = createExecution();

        assertEquals(LocalDate.now(), execution.getDate());
    }

    @Test
    void shouldChangeDate() {
        Execution execution = createExecution();

        LocalDate newDate = LocalDate.of(2020, 1, 1);
        execution.changeDate(newDate);

        assertEquals(newDate, execution.getDate());
    }

    @Test
    void shouldBindExerciseCorrectly() {
        Exercise exercise = createExercise();

        Execution execution = new Execution(exercise);

        assertSame(exercise, execution.getExercise());
    }

    @Test
    void shouldStartWithEmptyExecutionSets() {
        Execution execution = createExecution();

        assertEquals(0, execution.getExecutionSets().size());
    }

    @Test
    void shouldAllowAddingExecutionSets() {
        Execution execution = createExecution();

        ExecutionSet set1 = new ExecutionSet(execution, 1, 100, 10);
        ExecutionSet set2 = new ExecutionSet(execution, 2, 90, 8);

        execution.getExecutionSets().add(set1);
        execution.getExecutionSets().add(set2);

        assertEquals(2, execution.getExecutionSets().size());
    }

    @Test
    void shouldRemoveExecutionSet() {
        Execution execution = createExecution();

        ExecutionSet set = new ExecutionSet(execution, 1, 100, 10);

        execution.getExecutionSets().add(set);
        execution.getExecutionSets().remove(set);

        assertTrue(execution.getExecutionSets().isEmpty());
    }

    @Test
    void shouldBeIndependentInstances() {
        Execution e1 = createExecution();
        Execution e2 = createExecution();

        assertNotSame(e1, e2);
        assertNotSame(e1.getExercise(), e2.getExercise());
    }
}
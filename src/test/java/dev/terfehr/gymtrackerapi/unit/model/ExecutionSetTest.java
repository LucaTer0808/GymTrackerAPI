package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.model.*;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExecutionSetTest {

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
        Exercise exercise = createExercise();
        return new Execution(exercise, ZonedDateTime.now());
    }

    private ExecutionSet createSet() {
        return new ExecutionSet(
                createExecution(),
                1,
                100.0,
                10
        );
    }

    @Test
    void shouldCreateExecutionSetCorrectly() {
        ExecutionSet set = createSet();

        assertNotNull(set.getExecution());
        assertEquals(1, set.getNumberInExecution());
        assertEquals(100.0, set.getWeight());
        assertEquals(10, set.getReps());
    }

    @Test
    void shouldBindExecutionCorrectly() {
        Execution execution = createExecution();
        ExecutionSet set = new ExecutionSet(execution, 2, 80.0, 8);

        assertSame(execution, set.getExecution());
    }

    @Test
    void shouldStoreValuesCorrectly() {
        ExecutionSet set = new ExecutionSet(createExecution(), 3, 120.5, 5);

        assertEquals(3, set.getNumberInExecution());
        assertEquals(120.5, set.getWeight());
        assertEquals(5, set.getReps());
    }

    @Test
    void shouldAllowDifferentSetsSameExecution() {
        Execution execution = createExecution();

        ExecutionSet set1 = new ExecutionSet(execution, 1, 100, 10);
        ExecutionSet set2 = new ExecutionSet(execution, 2, 90, 8);

        assertEquals(set1.getExecution(), set2.getExecution());
        assertNotEquals(set1.getNumberInExecution(), set2.getNumberInExecution());
    }

    @Test
    void shouldBeIndependentObjects() {
        ExecutionSet set1 = createSet();
        ExecutionSet set2 = createSet();

        assertNotSame(set1, set2);
    }
}
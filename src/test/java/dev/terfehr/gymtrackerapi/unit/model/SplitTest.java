package dev.terfehr.gymtrackerapi.unit.model;

import dev.terfehr.gymtrackerapi.model.Day;
import dev.terfehr.gymtrackerapi.model.Split;
import dev.terfehr.gymtrackerapi.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SplitTest {

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
        User user = createUser();
        return new Split(user, "Push Pull Legs", List.of("Push", "Pull", "Legs"));
    }

    @Test
    void shouldCreateSplitCorrectly() {
        Split split = createSplit();

        assertEquals("Push Pull Legs", split.getName());
        assertEquals(3, split.getDays().size());
        assertNotNull(split.getUser());
    }

    @Test
    void shouldChangeName() {
        Split split = createSplit();

        split.changeName("New Split");

        assertEquals("New Split", split.getName());
    }

    @Test
    void shouldIgnoreNullNameChange() {
        Split split = createSplit();

        split.changeName(null);

        assertEquals("Push Pull Legs", split.getName());
    }

    @Test
    void shouldAddDay() {
        Split split = createSplit();

        Day newDay = split.addDay("Cardio");

        assertNotNull(newDay);
        assertEquals(4, split.getDays().size());
        assertTrue(split.getDays().stream()
                .anyMatch(day -> day.getName().equals("Cardio")));
    }

    @Test
    void shouldRemoveDay() {
        Split split = createSplit();

        Day dayToRemove = split.getDays().iterator().next();

        split.removeDay(dayToRemove);

        assertEquals(2, split.getDays().size());
        assertFalse(split.getDays().contains(dayToRemove));
    }

    @Test
    void shouldContainAllInitialDays() {
        Split split = createSplit();

        List<String> expected = List.of("Push", "Pull", "Legs");

        for (String name : expected) {
            assertTrue(split.getDays().stream()
                    .anyMatch(day -> day.getName().equals(name)));
        }
    }

    @Test
    void shouldFailAssertionIfDuplicateDayAdded() {
        Split split = createSplit();

        String existingDay = split.getDays().iterator().next().getName();

        assertThrows(AssertionError.class, () ->
                split.addDay(existingDay)
        );
    }

    @Test
    void shouldFailAssertionIfRemovingNonExistingDay() {
        Split split = createSplit();

        Day fakeDay = new Day(split, "Fake");

        assertThrows(AssertionError.class, () ->
                split.removeDay(fakeDay)
        );
    }
}
package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "splits")
@NullMarked
@NoArgsConstructor
public class Split {

    public static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Nullable
    private Long id;

    @Getter
    @OneToOne(mappedBy = "split", optional = false)
    private User user;

    @Getter
    @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Getter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "split", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<Day> days;

    public Split(User user, String name, List<String> dayNames) {
        this.user = user;
        this.name = name;

        Set<Day> days = new HashSet<>();
        for (String dayName : dayNames) {
            days.add(new Day(this, dayName));
        }

        this.days = days;
    }

    public void changeName(@Nullable String name) {
        if (name != null)
            this.name = name;
    }

    public Day addDay(String name) {
        assert this.days.stream().noneMatch(day -> day.getName().equals(name));

        Day day = new Day(this, name);
        days.add(day);
        return day;
    }

    public void removeDay(Day day) {
        assert this.days.contains(day);

        this.days.remove(day);
    }
}

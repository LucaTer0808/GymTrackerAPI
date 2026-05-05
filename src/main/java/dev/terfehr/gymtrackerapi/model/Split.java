package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "splits")
@NullMarked
@NoArgsConstructor
public class Split {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "split", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<Day> days;

    public Split(String name, Set<String> dayNames) {
        this.name = name;

        Set<Day> days = new HashSet<>();
        for (String dayName : dayNames) {
            days.add(new Day(this, dayName));
        }

        this.days = days;
    }
}

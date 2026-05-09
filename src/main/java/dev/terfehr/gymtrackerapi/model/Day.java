package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "days", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_name_per_user",
                columnNames = {"split_id", "name"}
        )
})
@NullMarked
@NoArgsConstructor
public class Day {

    public static final int MAX_NAME_LENGTH = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Nullable
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "split_id", nullable = false)
    @Getter
    private Split split;

    @Getter
    @Column(name = "name", nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Getter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "day", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ExerciseSlot> exerciseSlots;

    public Day(Split split, String name) {
        this.split = split;
        this.name = name;

        this.exerciseSlots = new HashSet<>();
    }

    public void addExerciseSlot(ExerciseSlot slot) {
        assert !this.exerciseSlots.contains(slot);
        this.exerciseSlots.add(slot);
    }

    public void removeExerciseSlot(ExerciseSlot slot) {
        assert this.exerciseSlots.contains(slot);
        this.exerciseSlots.remove(slot);
    }

    public void changeName(String name) {
        this.name = name;
    }
}

package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exercises", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_exercise_name_per_user",
                columnNames = {"name", "user_id"}
        )
})
@NullMarked
@NoArgsConstructor
public class Exercise {

    public static final int MAX_NAME_LENGTH = 50;

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exercise", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ExerciseSlot> exerciseSlots;

    @Getter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exercise", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Execution> executions;

    public Exercise(User user, String name)  {
        this.user = user;
        this.name = name;
        this.exerciseSlots = new HashSet<>();
        this.executions = new HashSet<>();
    }

    public void addExerciseSlot(ExerciseSlot slot) {
        assert this.exerciseSlots.contains(slot);
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

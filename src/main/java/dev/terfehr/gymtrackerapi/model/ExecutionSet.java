package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = "sets", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_set_number_per_execution",
                columnNames = {"execution_id", "number_in_exercise"}
        )
})
@NullMarked
@NoArgsConstructor
public class ExecutionSet {

    @Getter
    @Nullable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "execution_id",  nullable = false)
    private Execution execution;

    @Getter
    @Column(name = "number_in_exercise", nullable = false)
    private int numberInExercise;

    @Getter
    @Column(name = "weight", nullable = false)
    private double weight;

    @Getter
    @Column(name = "reps", nullable = false)
    private int reps;

    public ExecutionSet(Execution execution, int numberInExercise, double weight, int reps) {
        this.numberInExercise = numberInExercise;
        this.execution = execution;
        this.weight = weight;
        this.reps = reps;
    }
}

package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

@Entity
@Table(name = "exercise_slot", uniqueConstraints = {
        @UniqueConstraint(
                name = "one_day_and_exercise",
                columnNames = {"day_id", "exercise_id"}
        )
})
@NullMarked
@NoArgsConstructor
public class ExerciseSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne(optional = false, fetch = FetchType.LAZY,  cascade = CascadeType.PERSIST)
    @JoinColumn(name = "day_id")
    private Day day;

    @Getter
    @ManyToOne(optional = false, fetch =  FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    public ExerciseSlot(Day day, Exercise exercise) {
        this.day = day;
        this.exercise = exercise;
    }
}

package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

@Entity
@Table(name = "days")
@NullMarked
@NoArgsConstructor
public class Day {

    // TODO: A day has multiple exercises. If a day is deleted, the exercises should remain!
    // TODO: Add a mapping entity that maps day to exercise, like a ExerciseSlot for a day or so.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "split_id", nullable = false)
    private Split split;
}

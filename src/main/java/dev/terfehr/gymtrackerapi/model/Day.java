package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "split_id", nullable = false)
    private Split split;

    @Getter
    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "day", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ExerciseSlot> exerciseSlots;

    public Day(Split split, String name) {
        this.split = split;
        this.name = name;

        this.exerciseSlots = new HashSet<>();
    }
}

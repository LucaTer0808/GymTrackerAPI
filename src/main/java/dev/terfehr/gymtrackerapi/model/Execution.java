package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "executions")
@NullMarked
@NoArgsConstructor
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "exercise",  nullable = false)
    private Exercise exercise;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionSet> executionSets;

    public Execution(Exercise exercise, List<ExecutionSet> executionSets) {
        assert executionSets.stream().allMatch(executionSet -> this.equals(executionSet.getExecution()));

        this.exercise = exercise;
        this.executionSets = executionSets;
        this.date = LocalDate.now();
    }
}

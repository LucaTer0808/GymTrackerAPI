package dev.terfehr.gymtrackerapi.model;

import dev.terfehr.gymtrackerapi.utils.AssertionUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "executions")
@NullMarked
@NoArgsConstructor
public class Execution {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY,  optional = false)
    @JoinColumn(name = "exercise",  nullable = false)
    private Exercise exercise;

    @Getter
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Getter
    @Setter
    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionSet> executionSets;

    public Execution(Exercise exercise) {
        this.exercise = exercise;
        this.date = LocalDate.now();
        this.executionSets = new ArrayList<>(); // add sets before persisting!
    }

    public void changeDate(LocalDate date) {
        this.date = date;
    }
}

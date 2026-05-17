package dev.terfehr.gymtrackerapi.model;

import dev.terfehr.gymtrackerapi.utils.AssertionUtils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDate;
import java.time.ZonedDateTime;
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
    @Column(name = "date_time", nullable = false)
    private ZonedDateTime dateTime;

    @Getter
    @Setter
    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionSet> executionSets;

    public Execution(Exercise exercise, ZonedDateTime dateTime) {
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.executionSets = new ArrayList<>(); // add sets before persisting!
    }

    public void changeDate(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }
}

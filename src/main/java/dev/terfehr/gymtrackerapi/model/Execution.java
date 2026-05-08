package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

@Entity
@Table(name = "executions")
@NullMarked
@NoArgsConstructor
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise",  nullable = false)
    private Exercise exercise;

    // TODO: Add implementation here AND in the DTO!!!
}

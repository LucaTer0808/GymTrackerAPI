package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

@Entity
@Table(name = "splits")
@NullMarked
@NoArgsConstructor
public class Split {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "split", orphanRemoval = true, cascade = CascadeType.ALL)
    Set<Day> days;

    // TODO: A Split has Days and a days has exercises. if a split is deleted so are the days but NOT the exercises!!
}

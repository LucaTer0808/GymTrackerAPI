package dev.terfehr.gymtrackerapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

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

    public static final int MAX_USERNAME_LENGTH = 50;

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private @Nullable Long id;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Column(name = "name", nullable = false)
    private String name;

    // TODO: Add Set with Execution or some kind

    public Exercise(User user, String name)  {
        this.user = user;
        this.name = name;
    }
}

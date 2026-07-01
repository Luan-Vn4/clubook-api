package br.upe.booklubapi.domain.activities.entities.clubactivities;

import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.readinggoals.entities.ReadingGoal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="reading_goal_defined_activities")
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class ReadingGoalDefinedActivity extends ClubActivity {

    @OneToOne
    @NotNull
    @JoinColumn(name="reading_goal_id")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private ReadingGoal readingGoal;

    @PrePersist
    private void prePersist() {
        setActivityType(ActivityType.READING_GOAL_DEFINED);
    }

}

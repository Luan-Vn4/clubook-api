package br.upe.booklubapi.domain.activities.entities.clubactivities;

import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.meetings.entities.Meeting;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="meeting_defined_activities")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class MeetingDefinedActivity extends ClubActivity {

    @OneToOne
    @NotNull
    @JoinColumn(name="meeting_id")
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Meeting meeting;

    @PrePersist
    private void prePersist() {
        setActivityType(ActivityType.MEETING_DEFINED);
    }

}

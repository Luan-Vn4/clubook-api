package br.upe.booklubapi.domain.meetings.entities;

import br.upe.booklubapi.domain.readinggoals.entities.ReadingGoal;
import br.upe.booklubapi.utils.hibernate.GeometryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

@Entity
@Table(name="meetings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Meeting {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @OneToOne
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name="reading_goal_id")
    @NotNull
    private ReadingGoal readingGoal;

    @Column(name="address")
    @Size(max=255)
    @NotEmpty
    private String address;

    @Column(name="latlng", columnDefinition="GEOMETRY(POINT, 4326)")
    @Type(GeometryType.class)
    private Point latlng;

    @Column(name="meeting_date")
    @NotNull
    private LocalDateTime date;

    @CreationTimestamp
    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Meeting meeting = (Meeting) o;
        return getId() != null && Objects.equals(getId(), meeting.getId());
    }

    @Override
    public final int hashCode() {
        final int classHashcode = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();

        return Objects.hash(getId(), classHashcode);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Meeting.class.getSimpleName() + "[", "]")
            .add("id=" + getId())
            .add("readingGoalId=" + readingGoal.getId())
            .add("address='" + getAddress() + "'")
            .add("latlng=" + getLatlng())
            .toString();
    }

}

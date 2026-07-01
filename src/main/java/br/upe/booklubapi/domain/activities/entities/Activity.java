package br.upe.booklubapi.domain.activities.entities;

import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="activities")
@Inheritance(strategy= InheritanceType.JOINED)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public abstract class Activity {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

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
        Activity activity = (Activity) o;
        return getId() != null && Objects.equals(getId(), activity.getId());
    }

    @Override
    public final int hashCode() {
        final int classHash = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
        final int idHash = getId() != null ? getId().hashCode() : 0;
        return Objects.hash(classHash, idHash);
    }

}

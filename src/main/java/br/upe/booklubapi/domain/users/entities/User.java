package br.upe.booklubapi.domain.users.entities;

import java.util.*;
import br.upe.booklubapi.domain.clubs.entities.Club;
import br.upe.booklubapi.utils.hibernate.UUIDVarcharType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_entity")
public class User {

    @Id
    @Column(columnDefinition = "varchar(36)")
    @GeneratedValue(strategy = GenerationType.UUID)
    @Type(UUIDVarcharType.class)
    private UUID id;

    @Column(name = "email")
    @NotNull
    @Email
    private String email;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "first_Name")
    @NotNull
    private String firstName;

    @Column(name = "last_Name")
    @NotNull
    private String lastName;

    @ElementCollection
    @CollectionTable(
        name="user_attribute",
        joinColumns=@JoinColumn(name = "user_id")
    )
    @MapKeyColumn(name = "name")
    @Column(name = "value")
    @NotNull
    private Map<String, String> attributes;

    @ManyToMany(mappedBy="members")
    @NotNull
    @Setter(AccessLevel.PRIVATE)
    private Set<Club> clubs = new HashSet<>();

    public User(
        UUID id,
        String email,
        String username,
        String firstName,
        String lastName,
        Map<String, String> attributes
    ) {
        this(
            id,
            email,
            username,
            firstName,
            lastName,
            attributes,
            new HashSet<>()
        );
    }

    public String getImage() {
        return attributes.getOrDefault("imageUrl", "");
    }

    public void setImage(String image) {
        attributes.put("imageUrl", image);
    }

    public String getBirthDate() {
        return attributes.getOrDefault("birthDate", "");
    }

    public void setBirthDate(String birthDate) {
        attributes.put("birthDate", birthDate);
    }

    public boolean joinClub(Club club) {
        clubs.add(club);
        if (club.containsMember(this)) return true;
        return club.addMember(this);
    }

    public boolean leaveClub(Club club) {
        clubs.remove(club);
        if (!club.containsMember(this)) return true;
        return club.removeMember(this);
    }

    public Set<Club> getClubs() {
        return Set.copyOf(clubs);
    }

    public boolean isInClub(Club club) {
        return clubs.contains(club);
    }

    @PreRemove
    public void leaveAllClubs() {
        for (Club club : clubs) leaveClub(club);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return getId() != null
            && getId().equals(other.getId());
    }

    @Override
    public final int hashCode() {
        return getId().hashCode();
    }

}
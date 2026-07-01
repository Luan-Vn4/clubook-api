package br.upe.booklubapi.infra.activities.repositories;

import br.upe.booklubapi.domain.activities.entities.clubactivities.ClubActivity;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.activities.repositories.ClubActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface JpaClubActivityRepository
        extends JpaRepository<ClubActivity, UUID>, ClubActivityRepository {

    @Override
    Page<ClubActivity> findAllByClubId(UUID clubId, Pageable pageable);

    @Override
    @Query("SELECT ca FROM ClubActivity ca WHERE ca.club.id = :clubId AND ca.activityType IN :types")
    Page<ClubActivity> findAllByClubIdAndTypeIn(
        @Param("clubId") UUID clubId,
        @Param("types") Collection<ActivityType> types,
        Pageable pageable
    );

    @Override
    @Query("""
        SELECT ca FROM ClubActivity ca
        WHERE ca.club.id IN (
            SELECT c.id FROM User u JOIN u.clubs c WHERE u.id = :userId
        )
        ORDER BY ca.createdAt DESC
        """)
    Page<ClubActivity> findClubActivitiesForUser(
        @Param("userId") UUID userId,
        Pageable pageable
    );

    @Override
    @Query("""
        SELECT ca FROM ClubActivity ca
        WHERE ca.club.id IN (
            SELECT c.id FROM User u JOIN u.clubs c WHERE u.id = :userId
        ) AND ca.activityType IN :types
        ORDER BY ca.createdAt DESC
        """)
    Page<ClubActivity> findClubActivitiesForUserAndTypeIn(
        @Param("userId") UUID userId,
        @Param("types") Collection<ActivityType> types,
        Pageable pageable
    );

}

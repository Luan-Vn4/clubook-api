package br.upe.booklubapi.domain.activities.repositories;

import br.upe.booklubapi.domain.activities.entities.clubactivities.ClubActivity;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.UUID;

public interface ClubActivityRepository
        extends CrudRepository<ClubActivity, UUID> {

    Page<ClubActivity> findAllByClubId(UUID clubId, Pageable pageable);

    Page<ClubActivity> findAllByClubIdAndTypeIn(UUID clubId, Collection<ActivityType> types, Pageable pageable);

    Page<ClubActivity> findClubActivitiesForUser(UUID userId, Pageable pageable);

    Page<ClubActivity> findClubActivitiesForUserAndTypeIn(UUID userId, Collection<ActivityType> types, Pageable pageable);

}

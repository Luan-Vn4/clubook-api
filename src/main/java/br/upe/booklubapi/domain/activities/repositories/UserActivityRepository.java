package br.upe.booklubapi.domain.activities.repositories;

import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.activities.entities.useractivities.UserActivity;
import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.UUID;

public interface UserActivityRepository
        extends CrudRepository<UserActivity, UUID> {

    Page<UserActivity> findAllByUserId(UUID userId, Pageable pageable);

    Page<UserActivity> findAllByUserIdAndTypeIn(UUID userId, Collection<ActivityType> types, Pageable pageable);

}

package br.upe.booklubapi.infra.activities.repositories;

import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.activities.entities.useractivities.UserActivity;
import br.upe.booklubapi.domain.activities.repositories.UserActivityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.UUID;

public interface JpaUserActivityRepository
        extends JpaRepository<UserActivity, UUID>, UserActivityRepository {

    @Override
    Page<UserActivity> findAllByUserId(UUID userId, Pageable pageable);

    @Override
    @Query("SELECT ua FROM UserActivity ua WHERE ua.user.id = :userId AND ua.activityType IN :types")
    Page<UserActivity> findAllByUserIdAndTypeIn(
        @Param("userId") UUID userId,
        @Param("types") Collection<ActivityType> types,
        Pageable pageable
    );

}

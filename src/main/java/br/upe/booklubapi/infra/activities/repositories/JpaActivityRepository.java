package br.upe.booklubapi.infra.activities.repositories;

import br.upe.booklubapi.domain.activities.entities.Activity;
import br.upe.booklubapi.domain.activities.repositories.ActivityRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JpaActivityRepository
        extends JpaRepository<Activity, UUID>,
                ActivityRepository {

    @NotNull
    @Override
    default <S extends Activity> S save(@NotNull S entity) {
        return saveAndFlush(entity);
    }

    @NotNull
    @Override
    default <S extends Activity> List<S> saveAll(Iterable<S> entities) {
        return saveAllAndFlush(entities);
    }

}

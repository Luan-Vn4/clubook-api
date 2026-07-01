package br.upe.booklubapi.domain.activities.repositories;

import br.upe.booklubapi.domain.activities.entities.Activity;
import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import java.util.UUID;

public interface ActivityRepository
        extends CrudRepository<Activity, UUID> {
}

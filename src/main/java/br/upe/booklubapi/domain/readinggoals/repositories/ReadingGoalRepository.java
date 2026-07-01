package br.upe.booklubapi.domain.readinggoals.repositories;

import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import br.upe.booklubapi.domain.core.repositories.QueryableRepository;
import br.upe.booklubapi.domain.readinggoals.entities.ReadingGoal;
import java.util.Optional;
import java.util.UUID;

public interface ReadingGoalRepository
        extends CrudRepository<ReadingGoal, UUID>,
                QueryableRepository<ReadingGoal>,
                UserReadingGoalRepository {

    Optional<ReadingGoal> findClubCurrentReadingGoal(UUID clubId);

    Optional<ReadingGoal> findByClub_IdAndBookId(UUID clubId, String bookId);

}

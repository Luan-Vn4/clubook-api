package br.upe.booklubapi.domain.clubs.repositories;

import br.upe.booklubapi.domain.clubs.entities.Club;
import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import br.upe.booklubapi.domain.core.repositories.QueryableRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ClubRepository
    extends CrudRepository<Club, UUID>, QueryableRepository<Club>,
            ClubMembersRepository {

    Page<Club> findAllClubsByReadingGoalBookId(String bookId, Pageable pageable);

    Integer countClubMembers(UUID clubId);

    Integer countClubsThatFinishedBook(String bookId, LocalDate today);

    Integer countClubsCurrentlyReadingBook(String bookId, LocalDate today);

}

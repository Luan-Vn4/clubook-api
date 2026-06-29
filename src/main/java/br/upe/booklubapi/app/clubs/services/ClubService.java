package br.upe.booklubapi.app.clubs.services;

import br.upe.booklubapi.app.books.dtos.BookClubStatsDTO;
import br.upe.booklubapi.app.clubs.dtos.ClubDTO;
import br.upe.booklubapi.app.clubs.dtos.CreateClubDTO;
import br.upe.booklubapi.app.clubs.dtos.QueryClubDTO;
import br.upe.booklubapi.app.clubs.dtos.UpdateClubDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import java.util.UUID;

public interface ClubService {

    BookClubStatsDTO getBookClubStats(String bookId);

    ClubDTO create(CreateClubDTO dto);

    ClubDTO update(UpdateClubDTO dto, UUID id);

    void delete(UUID id);

    ClubDTO findById(UUID id);

    PagedModel<ClubDTO> findAll(Pageable pageable);

    PagedModel<ClubDTO> findAll(QueryClubDTO queryDTO, Pageable pageable);

}

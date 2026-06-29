package br.upe.booklubapi.app.readinggoals.services;

import br.upe.booklubapi.app.readinggoals.dtos.CreateReadingGoalDTO;
import br.upe.booklubapi.app.readinggoals.dtos.ReadingGoalDTO;
import br.upe.booklubapi.app.readinggoals.dtos.ReadingGoalQueryDTO;
import br.upe.booklubapi.app.readinggoals.dtos.ReviewReadingGoalDTO;
import br.upe.booklubapi.app.readinggoals.dtos.UpdateReadingGoalDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import java.util.UUID;

public interface ReadingGoalService {

    ReadingGoalDTO finishReadingGoal(UUID readingGoalId, ReviewReadingGoalDTO dto);

    void reviewReadingGoal(UUID readingGoalId, ReviewReadingGoalDTO dto);

    ReadingGoalDTO addReadingGoal(UUID clubId, CreateReadingGoalDTO dto);

    ReadingGoalDTO updateReadingGoal(
        UUID readingGoalId,
        UpdateReadingGoalDTO dto
    );

    PagedModel<ReadingGoalDTO> getReadingGoals(
        UUID clubId,
        Pageable pageable,
        ReadingGoalQueryDTO dto
    );

    PagedModel<ReadingGoalDTO> getUserReadingGoals(
        UUID userId,
        Pageable pageable,
        ReadingGoalQueryDTO dto
    );

    ReadingGoalDTO getClubCurrentReadingGoal(UUID clubId);

    ReadingGoalDTO getReadingGoal(UUID readingGoalId);

    void deleteReadingGoal(UUID id);

}

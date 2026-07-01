package br.upe.booklubapi.app.activities.useractivities.dtos;

import br.upe.booklubapi.app.activities.dtos.UserActivityDTO;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserCompletedReadingActivityDTO(
    ActivityType type,
    UUID id,
    LocalDateTime createdAt,
    UUID userId,
    String bookId,
    LocalDate startDate,
    LocalDate endDate,
    String bookTitle,
    String bookCoverUrl
) implements UserActivityDTO {
}

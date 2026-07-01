package br.upe.booklubapi.app.activities.clubactivities.dtos;

import br.upe.booklubapi.app.activities.dtos.ClubActivityDTO;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MemberCompletedReadingActivityDTO(
    ActivityType type,
    UUID id,
    LocalDateTime createdAt,
    UUID clubId,
    UUID userId,
    String bookId,
    LocalDate startDate,
    LocalDate endDate,
    String clubName,
    String clubPhotoUrl,
    String userName,
    String userAvatarUrl,
    String bookTitle,
    String bookCoverUrl
) implements ClubActivityDTO {
}

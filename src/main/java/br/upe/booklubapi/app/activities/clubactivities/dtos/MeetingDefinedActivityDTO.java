package br.upe.booklubapi.app.activities.clubactivities.dtos;

import br.upe.booklubapi.app.activities.dtos.ClubActivityDTO;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record MeetingDefinedActivityDTO(
    ActivityType type,
    UUID id,
    LocalDateTime createdAt,
    UUID clubId,
    UUID meetingId,
    String clubName,
    String clubPhotoUrl,
    String meetingAddress,
    LocalDate meetingDate,
    String bookId,
    String bookTitle,
    String bookCoverUrl
) implements ClubActivityDTO {
}

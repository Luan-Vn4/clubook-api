package br.upe.booklubapi.app.meetings.dtos;

import br.upe.booklubapi.utils.models.SimpleCoordinate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link br.upe.booklubapi.domain.meetings.entities.Meeting}
 */
public record CreateMeetingDTO(
    @NotNull
    UUID readingGoalId,
    @Size(max = 255)
    @NotEmpty
    String address,
    @NotNull
    SimpleCoordinate latlng,
    @NotNull
    LocalDateTime date
) implements Serializable {}
package br.upe.booklubapi.app.readinggoals.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReadingGoalDTO(
    @NotNull
    UUID id,
    @NotEmpty
    @Size(max = 32)
    String bookId,
    @NotNull
    UUID clubId,
    @NotNull
    LocalDate startDate,
    @NotNull
    LocalDate endDate,
    @NotNull
    Boolean finished,
    @NotNull
    LocalDateTime createdAt
) implements Serializable {}
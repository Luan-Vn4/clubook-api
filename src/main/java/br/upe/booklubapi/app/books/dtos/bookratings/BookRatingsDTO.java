package br.upe.booklubapi.app.books.dtos.bookratings;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record BookRatingsDTO(
    @NotNull
    UUID userId,
    @NotNull
    String bookId,
    @NotNull
    @Min(0)
    @Max(5)
    Integer rating,
    @NotNull
    @Min(0)
    @Max(5)
    Integer difficulty,
    @Nullable
    String review,
    @NotNull
    LocalDateTime createdAt
) {}

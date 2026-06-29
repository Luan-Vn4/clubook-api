package br.upe.booklubapi.app.readinggoals.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * A star rating (1-5) plus an optional written review (resenha) for the book
 * of a reading goal.
 */
public record ReviewReadingGoalDTO(
    @NotNull
    @Min(1)
    @Max(5)
    Short rating,
    @Size(max = 500)
    String review
) implements Serializable {}

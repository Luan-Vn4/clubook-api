package br.upe.booklubapi.app.books.dtos;

import java.io.Serializable;

/**
 * Aggregated club reading stats for a given book.
 *
 * @param alreadyRead      number of clubs whose reading goal for the book has ended
 * @param currentlyReading number of clubs whose reading goal for the book contains today
 */
public record BookClubStatsDTO(
    int alreadyRead,
    int currentlyReading
) implements Serializable {}

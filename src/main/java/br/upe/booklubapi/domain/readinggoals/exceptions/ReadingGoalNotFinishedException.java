package br.upe.booklubapi.domain.readinggoals.exceptions;

import java.util.UUID;

/**
 * Thrown when a member tries to review a reading goal whose reading period has
 * not ended yet.
 */
public class ReadingGoalNotFinishedException extends RuntimeException {

    public ReadingGoalNotFinishedException(UUID readingGoalId) {
        super(
            "The reading period for reading goal %s has not ended yet."
                .formatted(readingGoalId)
        );
    }
}

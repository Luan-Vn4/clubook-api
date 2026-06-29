package br.upe.booklubapi.presentation.exceptions.readingoals;

import br.upe.booklubapi.domain.readinggoals.exceptions.ConflictingReadingGoalException;
import br.upe.booklubapi.domain.readinggoals.exceptions.IllegalReadingGoalDate;
import br.upe.booklubapi.domain.readinggoals.exceptions.NoCurrentReadingGoalException;
import br.upe.booklubapi.domain.readinggoals.exceptions.ReadingGoalNotFinishedException;
import br.upe.booklubapi.domain.readinggoals.exceptions.ReadingGoalNotFoundException;
import br.upe.booklubapi.presentation.exceptions.core.ExceptionBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ReadingGoalExceptionHandler {

    @ExceptionHandler(ConflictingReadingGoalException.class)
    public ResponseEntity<ExceptionBody> handle(ConflictingReadingGoalException e) {
        final HttpStatus status = HttpStatus.CONFLICT;

        final var resp = ExceptionBody.builder()
            .httpStatus(status.value())
            .error("Conflicting dates")
            .message(e.getMessage())
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(status).body(resp);
    }

    @ExceptionHandler(ReadingGoalNotFoundException.class)
    public ResponseEntity<ExceptionBody> handle(ReadingGoalNotFoundException e) {
        final HttpStatus status = HttpStatus.NOT_FOUND;

        final var resp = ExceptionBody.builder()
            .httpStatus(status.value())
            .error("Reading Goal Not Found")
            .message(e.getMessage())
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(status).body(resp);
    }

    @ExceptionHandler(NoCurrentReadingGoalException.class)
    public ResponseEntity<ExceptionBody> handle(NoCurrentReadingGoalException e) {
        final HttpStatus status = HttpStatus.NO_CONTENT;

        final var resp = ExceptionBody.builder()
            .httpStatus(status.value())
            .error("No Current Reading Goal Defined")
            .message(e.getMessage())
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(status).body(resp);
    }

    @ExceptionHandler(ReadingGoalNotFinishedException.class)
    public ResponseEntity<ExceptionBody> handle(ReadingGoalNotFinishedException e) {
        final HttpStatus status = HttpStatus.FORBIDDEN;

        final var resp = ExceptionBody.builder()
            .httpStatus(status.value())
            .error("Reading period not finished")
            .message(e.getMessage())
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(status).body(resp);
    }

    @ExceptionHandler(IllegalReadingGoalDate.class)
    public ResponseEntity<ExceptionBody> handle(IllegalReadingGoalDate e) {
        final HttpStatus status = HttpStatus.BAD_REQUEST;

        final var resp = ExceptionBody.builder()
            .httpStatus(status.value())
            .error("Start date must come before end date")
            .message(e.getMessage())
            .timestamp(Instant.now())
            .build();

        return ResponseEntity.status(status).body(resp);
    }

}

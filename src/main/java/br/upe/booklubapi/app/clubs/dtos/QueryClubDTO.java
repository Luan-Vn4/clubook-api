package br.upe.booklubapi.app.clubs.dtos;

import br.upe.booklubapi.domain.clubs.entities.QClub;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public record QueryClubDTO(
    Optional<String> name,
    Optional<LocalDate> startDate,
    Optional<LocalDate> endDate,
    Optional<Boolean> isPrivate,
    Optional<UUID> ownerId
) {

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public BooleanExpression getQuery(QClub club) {
        var containsName = this.name()
            .filter(name -> !name.isBlank())
            .map(club.name::containsIgnoreCase)
            .orElse(Expressions.TRUE);
        
        var creationDateBetween = this.hasValidDateInterval()
            ? club.creationDate.between(
            this.startDate().get(), this.endDate().get())
            : Expressions.TRUE;

        var isPrivate = this.isPrivate()
            .map(club.isPrivate::eq)
            .orElse(Expressions.TRUE);

        var ownerId = this.ownerId()
            .map(club.owner.id::eq)
            .orElse(Expressions.TRUE);

        return containsName
            .and(creationDateBetween
                .and(isPrivate)
                    .and(ownerId));
    }

    private boolean hasValidDateInterval() {
        return startDate.isPresent()
            && endDate.isPresent()
            && startDate.get().isBefore(endDate.get());
    }

}

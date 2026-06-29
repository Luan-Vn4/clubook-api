package br.upe.booklubapi.app.meetings.dtos;

import br.upe.booklubapi.domain.meetings.entities.Meeting;
import br.upe.booklubapi.utils.models.SimpleCoordinate;
import org.locationtech.jts.geom.Point;
import org.mapstruct.*;

@Mapper(
    unmappedTargetPolicy=ReportingPolicy.IGNORE,
    componentModel=MappingConstants.ComponentModel.SPRING
)
public interface MeetingDTOMapper {

    @Mapping(source="readingGoal.id", target="readingGoalId")
    @Mapping(source="readingGoal.club.id", target="clubId")
    MeetingDTO toDto(Meeting meeting);

    default SimpleCoordinate pointToSimpleCoordinate(Point  point) {
        return new SimpleCoordinate(
            point.getY(),
            point.getX()
        );
    }

}
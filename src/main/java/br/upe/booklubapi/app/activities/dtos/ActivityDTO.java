package br.upe.booklubapi.app.activities.dtos;

import br.upe.booklubapi.app.activities.clubactivities.dtos.MeetingDefinedActivityDTO;
import br.upe.booklubapi.app.activities.clubactivities.dtos.MemberCompletedReadingActivityDTO;
import br.upe.booklubapi.app.activities.clubactivities.dtos.ReadingGoalDefinedActivityDTO;
import br.upe.booklubapi.app.activities.useractivities.dtos.UserCompletedReadingActivityDTO;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(
    discriminatorProperty = "type",
    oneOf = {
        MeetingDefinedActivityDTO.class,
        MemberCompletedReadingActivityDTO.class,
        ReadingGoalDefinedActivityDTO.class,
        UserCompletedReadingActivityDTO.class
    }
)
public interface ActivityDTO {

    UUID id();

    LocalDateTime createdAt();

    ActivityType type();

}

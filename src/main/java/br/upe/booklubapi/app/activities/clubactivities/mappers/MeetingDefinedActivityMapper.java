package br.upe.booklubapi.app.activities.clubactivities.mappers;

import br.upe.booklubapi.app.activities.clubactivities.dtos.MeetingDefinedActivityDTO;
import br.upe.booklubapi.app.activities.mappers.ActivityDTOMapper;
import br.upe.booklubapi.app.books.services.GoogleBooksService;
import br.upe.booklubapi.domain.activities.entities.Activity;
import br.upe.booklubapi.domain.activities.entities.clubactivities.MeetingDefinedActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public abstract class MeetingDefinedActivityMapper
        implements ActivityDTOMapper<MeetingDefinedActivityDTO> {

    @Autowired
    protected GoogleBooksService googleBooksService;

    @Mapping(source="activityType", target="type")
    @Mapping(source="meeting.id", target="meetingId")
    @Mapping(source="club.id", target="clubId")
    @Mapping(source="club.name", target="clubName")
    @Mapping(source="club.imageUrl", target="clubPhotoUrl")
    @Mapping(source="meeting.address", target="meetingAddress")
    @Mapping(target="meetingDate", ignore=true)
    @Mapping(target="bookId", ignore=true)
    @Mapping(target="bookTitle", ignore=true)
    @Mapping(target="bookCoverUrl", ignore=true)
    abstract MeetingDefinedActivityDTO mapToDTO(MeetingDefinedActivity entity);

    @Override
    public MeetingDefinedActivityDTO toDTO(Activity activity) {
        if (!canConvert(activity)) throw new IllegalArgumentException(
            "Cannot convert activity to MeetingDefinedActivityDTO"
        );

        final MeetingDefinedActivity entity = (MeetingDefinedActivity) activity;
        final MeetingDefinedActivityDTO base = mapToDTO(entity);

        String bookId = null;
        String bookTitle = null;
        String bookCoverUrl = null;
        LocalDate meetingDate = null;

        try {
            if (entity.getMeeting() != null && entity.getMeeting().getReadingGoal() != null) {
                bookId = entity.getMeeting().getReadingGoal().getBookId();
                meetingDate = entity.getMeeting().getReadingGoal().getStartDate();
                if (bookId != null) {
                    var book = googleBooksService.getBookById(bookId);
                    bookTitle = book.getTitle();
                    bookCoverUrl = book.getThumbnail();
                }
            }
        } catch (Exception ignored) {
            // Best effort — enrichment failure never causes 500
        }

        return new MeetingDefinedActivityDTO(
            base.type(),
            base.id(),
            base.createdAt(),
            base.clubId(),
            base.meetingId(),
            base.clubName(),
            base.clubPhotoUrl(),
            base.meetingAddress(),
            meetingDate,
            bookId,
            bookTitle,
            bookCoverUrl
        );
    }

    @Override
    public boolean canConvert(Activity activity) {
        return activity instanceof MeetingDefinedActivity;
    }

}

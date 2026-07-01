package br.upe.booklubapi.app.activities.clubactivities.mappers;

import br.upe.booklubapi.app.activities.clubactivities.dtos.ReadingGoalDefinedActivityDTO;
import br.upe.booklubapi.app.activities.mappers.ActivityDTOMapper;
import br.upe.booklubapi.app.books.services.GoogleBooksService;
import br.upe.booklubapi.domain.activities.entities.Activity;
import br.upe.booklubapi.domain.activities.entities.clubactivities.ReadingGoalDefinedActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ReadingGoalDefinedActivityMapper
        implements ActivityDTOMapper<ReadingGoalDefinedActivityDTO> {

    @Autowired
    protected GoogleBooksService googleBooksService;

    @Mapping(source = "activityType", target = "type")
    @Mapping(source = "readingGoal.id", target = "readingGoalId")
    @Mapping(source = "club.id", target = "clubId")
    @Mapping(source = "club.name", target = "clubName")
    @Mapping(source = "club.imageUrl", target = "clubPhotoUrl")
    @Mapping(source = "readingGoal.startDate", target = "goalStartDate")
    @Mapping(source = "readingGoal.endDate", target = "goalEndDate")
    @Mapping(target = "bookTitle", ignore = true)
    @Mapping(target = "bookCoverUrl", ignore = true)
    abstract ReadingGoalDefinedActivityDTO mapToDTO(ReadingGoalDefinedActivity entity);

    @Override
    public ReadingGoalDefinedActivityDTO toDTO(Activity activity) {
        if (!canConvert(activity)) throw new IllegalArgumentException(
            "Cannot convert activity to ReadingGoalDefinedActivityDTO"
        );

        final ReadingGoalDefinedActivity entity = (ReadingGoalDefinedActivity) activity;
        final ReadingGoalDefinedActivityDTO base = mapToDTO(entity);

        String bookTitle = null;
        String bookCoverUrl = null;

        try {
            if (entity.getReadingGoal() != null) {
                final String bookId = entity.getReadingGoal().getBookId();
                if (bookId != null) {
                    var book = googleBooksService.getBookById(bookId);
                    bookTitle = book.getTitle();
                    bookCoverUrl = book.getThumbnail();
                }
            }
        } catch (Exception ignored) {
            // Best effort — enrichment failure never causes 500
        }

        return new ReadingGoalDefinedActivityDTO(
            base.type(),
            base.id(),
            base.createdAt(),
            base.clubId(),
            base.readingGoalId(),
            base.clubName(),
            base.clubPhotoUrl(),
            base.goalStartDate(),
            base.goalEndDate(),
            bookTitle,
            bookCoverUrl
        );
    }

    @Override
    public boolean canConvert(Activity activity) {
        return activity instanceof ReadingGoalDefinedActivity;
    }

}

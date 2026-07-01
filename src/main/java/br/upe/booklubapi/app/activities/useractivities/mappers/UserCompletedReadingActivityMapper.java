package br.upe.booklubapi.app.activities.useractivities.mappers;

import br.upe.booklubapi.app.activities.mappers.ActivityDTOMapper;
import br.upe.booklubapi.app.activities.useractivities.dtos.UserCompletedReadingActivityDTO;
import br.upe.booklubapi.app.books.services.GoogleBooksService;
import br.upe.booklubapi.domain.activities.entities.Activity;
import br.upe.booklubapi.domain.activities.entities.useractivities.UserCompletedReadingActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserCompletedReadingActivityMapper
        implements ActivityDTOMapper<UserCompletedReadingActivityDTO> {

    @Autowired
    protected GoogleBooksService googleBooksService;

    @Mapping(source = "activityType", target = "type")
    @Mapping(source = "bookUser.user.id", target = "userId")
    @Mapping(source = "bookUser.id.bookId", target = "bookId")
    @Mapping(target = "bookTitle", ignore = true)
    @Mapping(target = "bookCoverUrl", ignore = true)
    abstract UserCompletedReadingActivityDTO mapToDTO(UserCompletedReadingActivity entity);

    @Override
    public UserCompletedReadingActivityDTO toDTO(Activity activity) {
        if (!canConvert(activity)) throw new IllegalArgumentException(
            "Cannot convert activity to UserCompletedReadingActivityDTO"
        );

        final UserCompletedReadingActivity entity = (UserCompletedReadingActivity) activity;
        final UserCompletedReadingActivityDTO base = mapToDTO(entity);

        String bookTitle = null;
        String bookCoverUrl = null;

        try {
            final String bookId = entity.getBookUser().getId().getBookId();
            if (bookId != null) {
                var book = googleBooksService.getBookById(bookId);
                bookTitle = book.getTitle();
                bookCoverUrl = book.getThumbnail();
            }
        } catch (Exception ignored) {
            // Best effort — enrichment failure never causes 500
        }

        return new UserCompletedReadingActivityDTO(
            base.type(),
            base.id(),
            base.createdAt(),
            base.userId(),
            base.bookId(),
            base.startDate(),
            base.endDate(),
            bookTitle,
            bookCoverUrl
        );
    }

    @Override
    public boolean canConvert(Activity activity) {
        return activity instanceof UserCompletedReadingActivity;
    }

}

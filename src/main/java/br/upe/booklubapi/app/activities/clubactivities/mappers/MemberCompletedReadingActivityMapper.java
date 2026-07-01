package br.upe.booklubapi.app.activities.clubactivities.mappers;

import br.upe.booklubapi.app.activities.clubactivities.dtos.MemberCompletedReadingActivityDTO;
import br.upe.booklubapi.app.activities.mappers.ActivityDTOMapper;
import br.upe.booklubapi.app.books.services.GoogleBooksService;
import br.upe.booklubapi.domain.activities.entities.Activity;
import br.upe.booklubapi.domain.activities.entities.clubactivities.MemberCompletedReadingActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MemberCompletedReadingActivityMapper
        implements ActivityDTOMapper<MemberCompletedReadingActivityDTO> {

    @Autowired
    protected GoogleBooksService googleBooksService;

    @Mapping(source = "activityType", target = "type")
    @Mapping(source = "bookUser.user.id", target = "userId")
    @Mapping(source = "bookUser.id.bookId", target = "bookId")
    @Mapping(source = "club.id", target = "clubId")
    @Mapping(source = "club.name", target = "clubName")
    @Mapping(source = "club.imageUrl", target = "clubPhotoUrl")
    @Mapping(target = "userName", expression = "java(entity.getBookUser().getUser().getFirstName() + \" \" + entity.getBookUser().getUser().getLastName())")
    @Mapping(source = "bookUser.user.image", target = "userAvatarUrl")
    @Mapping(target = "bookTitle", ignore = true)
    @Mapping(target = "bookCoverUrl", ignore = true)
    abstract MemberCompletedReadingActivityDTO mapToDTO(MemberCompletedReadingActivity entity);

    @Override
    public MemberCompletedReadingActivityDTO toDTO(Activity activity) {
        if (!canConvert(activity)) throw new IllegalArgumentException(
            "Cannot convert activity to MemberCompletedReadingActivityDTO"
        );

        final MemberCompletedReadingActivity entity = (MemberCompletedReadingActivity) activity;
        final MemberCompletedReadingActivityDTO base = mapToDTO(entity);

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

        return new MemberCompletedReadingActivityDTO(
            base.type(),
            base.id(),
            base.createdAt(),
            base.clubId(),
            base.userId(),
            base.bookId(),
            base.startDate(),
            base.endDate(),
            base.clubName(),
            base.clubPhotoUrl(),
            base.userName(),
            base.userAvatarUrl(),
            bookTitle,
            bookCoverUrl
        );
    }

    @Override
    public boolean canConvert(Activity activity) {
        return activity instanceof MemberCompletedReadingActivity;
    }

}

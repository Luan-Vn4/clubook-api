package br.upe.booklubapi.app.readinggoals.services;

import br.upe.booklubapi.app.books.dtos.BookUserDTO;
import br.upe.booklubapi.app.books.dtos.bookratings.CreateBookRatingsDTO;
import br.upe.booklubapi.app.books.services.BookRatingsService;
import br.upe.booklubapi.app.books.services.BookUserService;
import br.upe.booklubapi.app.readinggoals.dtos.*;
import br.upe.booklubapi.domain.activities.entities.clubactivities.ReadingGoalDefinedActivity;
import br.upe.booklubapi.domain.activities.repositories.ActivityRepository;
import br.upe.booklubapi.domain.books.entities.BookUserId;
import br.upe.booklubapi.domain.clubs.entities.Club;
import br.upe.booklubapi.domain.clubs.exceptions.ClubNotFoundException;
import br.upe.booklubapi.domain.clubs.exceptions.UnauthorizedClubActionException;
import br.upe.booklubapi.domain.clubs.repositories.ClubRepository;
import br.upe.booklubapi.domain.readinggoals.entities.QReadingGoal;
import br.upe.booklubapi.domain.readinggoals.entities.ReadingGoal;
import br.upe.booklubapi.domain.readinggoals.exceptions.ConflictingReadingGoalException;
import br.upe.booklubapi.domain.readinggoals.exceptions.IllegalReadingGoalDate;
import br.upe.booklubapi.domain.readinggoals.exceptions.NoCurrentReadingGoalException;
import br.upe.booklubapi.domain.readinggoals.exceptions.ReadingGoalNotFinishedException;
import br.upe.booklubapi.domain.readinggoals.exceptions.ReadingGoalNotFoundException;
import br.upe.booklubapi.domain.readinggoals.repositories.ReadingGoalRepository;
import br.upe.booklubapi.domain.users.entities.User;
import br.upe.booklubapi.domain.users.exceptions.UserNotFoundException;
import br.upe.booklubapi.domain.users.repository.UserRepository;
import br.upe.booklubapi.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReadingGoalServiceImpl implements ReadingGoalService {

    private final CreateReadingGoalDTOMapper createReadingGoalDTOMapper;

    private final UpdateReadingGoalDTOMapper updateReadingGoalDTOMapper;

    private final ReadingGoalDTOMapper readingGoalDTOMapper;

    private final UserUtils userUtils;

    private final ClubRepository clubRepository;

    private final ReadingGoalRepository readingGoalRepository;

    private final QReadingGoal readingGoal = QReadingGoal.readingGoal;

    private final UserRepository userRepository;

    private final ActivityRepository activityRepository;

    private final BookUserService bookUserService;

    private final BookRatingsService bookRatingsService;

    private User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException(id)
        );
    }

    private Club getClub(UUID id) {
        return clubRepository.findById(id).orElseThrow(
            () -> new ClubNotFoundException(id)
        );
    }

    private ReadingGoal getReadingGoalById(UUID id) {
        return readingGoalRepository.findById(id).orElseThrow(
            () -> new ReadingGoalNotFoundException(id)
        );
    }

    private void checkConflictingReadingGoalDates(
        UUID clubId,
        LocalDate startDate,
        LocalDate endDate,
        Optional<UUID> excludeId
    ) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalReadingGoalDate(startDate, endDate);
        }

        var query = (
            readingGoal.startDate.goe(startDate)
            .and(readingGoal.startDate.loe(endDate))
        ).or(
            readingGoal.endDate.goe(startDate)
            .and(readingGoal.endDate.loe(endDate))
        ).and(readingGoal.club.id.eq(clubId));

        if (excludeId.isPresent()) {
            query = query.and(readingGoal.id.ne(excludeId.get()));
        }

        final boolean conflicts = readingGoalRepository.exists(query);

        if (conflicts) {
            throw new ConflictingReadingGoalException(startDate, endDate);
        }
    }

    @Override
    @Transactional
    public ReadingGoalDTO addReadingGoal(
        UUID clubId,
        CreateReadingGoalDTO dto
    ) {
        final var loggedUserId = userUtils.getLoggedUserId();
        final Club club = getClub(clubId);

        if (!club.getOwner().getId().equals(loggedUserId)) {
            throw new UnauthorizedClubActionException(
                "Add Reading Goal",
                loggedUserId,
                clubId
            );
        }

        checkConflictingReadingGoalDates(
            clubId,
            dto.startDate(),
            dto.endDate(),
            Optional.empty()
        );

        final ReadingGoal readingGoal = createReadingGoalDTOMapper.toEntity(
            dto,
            club
        );
        final ReadingGoal createdReadingGoal = readingGoalRepository.save(readingGoal);

        publishReadingGoal(createdReadingGoal);

        return readingGoalDTOMapper.toDto(
            createdReadingGoal
        );
    }

    private void publishReadingGoal(ReadingGoal readingGoal) {
        final var activity = ReadingGoalDefinedActivity.builder()
            .readingGoal(readingGoal)
            .club(readingGoal.getClub())
            .build();

        activityRepository.save(activity);
    }

    @Override
    @Transactional
    public ReadingGoalDTO updateReadingGoal(
        UUID readingGoalId,
        UpdateReadingGoalDTO dto
    ) {
        final var loggedUserId = userUtils.getLoggedUserId();
        final ReadingGoal readingGoal = getReadingGoalById(readingGoalId);
        final Club club = readingGoal.getClub();

        if (!club.getOwner().getId().equals(loggedUserId)) {
            throw new UnauthorizedClubActionException(
                "Update Reading Goal",
                loggedUserId,
                club.getId()
            );
        }

        checkConflictingReadingGoalDates(
            club.getId(),
            dto.startDate(),
            dto.endDate(),
            Optional.of(readingGoalId)
        );

        ReadingGoal updated = updateReadingGoalDTOMapper.partialUpdate(
            dto,
            readingGoal
        );

        return readingGoalDTOMapper.toDto(readingGoalRepository.save(updated));
    }

    @Override
    public PagedModel<ReadingGoalDTO> getReadingGoals(
        UUID clubId,
        Pageable pageable,
        ReadingGoalQueryDTO dto
    ) {
        QReadingGoal readingGoal = QReadingGoal.readingGoal;
        User loggedUser = getUser(userUtils.getLoggedUserId());
        Club club = getClub(clubId);

        // Only private clubs restrict reading goals to members.
        if (Boolean.TRUE.equals(club.getIsPrivate()) && !loggedUser.isInClub(club)) {
            throw new UnauthorizedClubActionException(
                "Get Reading Goals",
                loggedUser.getId(),
                clubId
            );
        }

        return new PagedModel<>(
            readingGoalRepository.findAll(
                dto.getQuery(readingGoal)
                    .and(readingGoal.club.id.eq(club.getId())),
                pageable
            ).map(readingGoalDTOMapper::toDto)
        );
    }

    @Override
    public PagedModel<ReadingGoalDTO> getUserReadingGoals(
        UUID userId,
        Pageable pageable,
        ReadingGoalQueryDTO dto
    ) {
        return new PagedModel<>(
            readingGoalRepository.findUserReadingGoals(
                userId,
                dto.getQuery(readingGoal),
                pageable
            ).map(readingGoalDTOMapper::toDto)
        );
    }

    @Override
    public ReadingGoalDTO getClubCurrentReadingGoal(UUID clubId) {
        final ReadingGoal readingGoal = readingGoalRepository
            .findClubCurrentReadingGoal(clubId)
            .orElseThrow(() -> new NoCurrentReadingGoalException(clubId));
        return readingGoalDTOMapper.toDto(readingGoal);
    }

    @Override
    @Transactional
    public ReadingGoalDTO finishReadingGoal(
        UUID readingGoalId,
        ReviewReadingGoalDTO dto
    ) {
        final UUID loggedUserId = userUtils.getLoggedUserId();
        final ReadingGoal readingGoal = getReadingGoalById(readingGoalId);
        final Club club = readingGoal.getClub();

        // Only the club owner can finish a reading goal.
        if (!club.getOwner().getId().equals(loggedUserId)) {
            throw new UnauthorizedClubActionException(
                "Finish Reading Goal",
                loggedUserId,
                club.getId()
            );
        }

        readingGoal.setFinished(true);
        final ReadingGoal saved = readingGoalRepository.save(readingGoal);

        saveBookReview(loggedUserId, readingGoal.getBookId(), dto);

        return readingGoalDTOMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void reviewReadingGoal(
        UUID readingGoalId,
        ReviewReadingGoalDTO dto
    ) {
        final UUID loggedUserId = userUtils.getLoggedUserId();
        final User loggedUser = getUser(loggedUserId);
        final ReadingGoal readingGoal = getReadingGoalById(readingGoalId);
        final Club club = readingGoal.getClub();

        if (!loggedUser.isInClub(club)) {
            throw new UnauthorizedClubActionException(
                "Review Reading Goal",
                loggedUserId,
                club.getId()
            );
        }

        // Members can only review once the reading period has ended.
        if (!LocalDate.now().isAfter(readingGoal.getEndDate())) {
            throw new ReadingGoalNotFinishedException(readingGoalId);
        }

        saveBookReview(loggedUserId, readingGoal.getBookId(), dto);
    }

    private void saveBookReview(
        UUID userId,
        String bookId,
        ReviewReadingGoalDTO dto
    ) {
        // book_ratings has a FK to book_user, so make sure that row exists
        // (and mark the book as read) before saving the rating.
        ensureBookInUserLibrary(userId, bookId);

        bookRatingsService.save(
            new BookUserId(bookId, userId),
            new CreateBookRatingsDTO(dto.rating(), (short) 0, dto.review())
        );
    }

    private void ensureBookInUserLibrary(UUID userId, String bookId) {
        try {
            bookUserService.findById(userId, bookId);
        } catch (RuntimeException e) {
            bookUserService.save(new BookUserDTO(userId, bookId, 1.0));
        }
    }

    @Override
    public ReadingGoalDTO getReadingGoal(UUID readingGoalId) {
        User loggedUser = getUser(userUtils.getLoggedUserId());
        ReadingGoal readingGoal = getReadingGoalById(readingGoalId);
        Club club = readingGoal.getClub();

        // Only private clubs restrict reading goals to members.
        if (Boolean.TRUE.equals(club.getIsPrivate()) && !loggedUser.isInClub(club)) {
            throw new UnauthorizedClubActionException(
                "Get Reading Goals",
                loggedUser.getId(),
                club.getId()
            );
        }

        return readingGoalDTOMapper.toDto(readingGoal);
    }

    @Override
    @Transactional
    public void deleteReadingGoal(UUID id) {
        User loggedUser = getUser(userUtils.getLoggedUserId());
        ReadingGoal readingGoal = getReadingGoalById(id);
        Club club = readingGoal.getClub();

        if (!loggedUser.isInClub(club) || !club.getOwner().equals(loggedUser)) {
            throw new UnauthorizedClubActionException(
                "Delete Reading Goal",
                loggedUser.getId(),
                club.getId()
            );
        }

        readingGoalRepository.deleteById(id);
    }

}

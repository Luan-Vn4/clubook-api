package br.upe.booklubapi.app.books.services;

import br.upe.booklubapi.app.books.dtos.BookUserDTO;
import br.upe.booklubapi.app.books.dtos.BookUserDTOMapper;
import br.upe.booklubapi.app.books.dtos.UpdateBookUserDTOMapper;
import br.upe.booklubapi.domain.activities.entities.clubactivities.MemberCompletedReadingActivity;
import br.upe.booklubapi.domain.activities.entities.useractivities.UserCompletedReadingActivity;
import br.upe.booklubapi.domain.activities.repositories.ActivityRepository;
import br.upe.booklubapi.domain.books.entities.BookUser;
import br.upe.booklubapi.domain.books.entities.BookUserId;
import br.upe.booklubapi.domain.books.exceptions.BookUserNotFoundException;
import br.upe.booklubapi.domain.books.repositories.BookProgressRepository;
import br.upe.booklubapi.domain.clubs.entities.Club;
import br.upe.booklubapi.domain.clubs.repositories.ClubRepository;
import br.upe.booklubapi.domain.readinggoals.entities.ReadingGoal;
import br.upe.booklubapi.domain.readinggoals.repositories.ReadingGoalRepository;
import br.upe.booklubapi.domain.users.entities.User;
import br.upe.booklubapi.domain.users.exceptions.UserNotFoundException;
import br.upe.booklubapi.domain.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookUserServiceImpl implements BookUserService {

    private final BookProgressRepository bookUserRepository;

    private final BookUserDTOMapper bookUserDTOMapper;

    private final UpdateBookUserDTOMapper updateBookUserDTOMapper;

    private final ActivityRepository activityRepository;

    private final ClubRepository clubRepository;

    private final ReadingGoalRepository readingGoalRepository;

    private final UserRepository userRepository;

    private User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new UserNotFoundException(userId)
        );
    }

    @Override
    @Transactional
    public BookUserDTO save(BookUserDTO saveProgressDTO) {
        final BookUser progressEntity = bookUserDTOMapper
            .toEntity(saveProgressDTO);
        
        final BookUser savedProgress = bookUserRepository.save(progressEntity);

        if (savedProgress.hasFinishedBook()) {
            publishCompletedReadingActivities(savedProgress);
        }

        return bookUserDTOMapper.toDTO(savedProgress);
    }

    private void publishCompletedReadingActivities(BookUser bookUser) {
        final User user = getUser(bookUser.getId().getUserId());
        final String bookId = bookUser.getId().getBookId();

        final List<Club> clubsReadingBook = clubRepository
            .findAllClubsByReadingGoalBookId(
                bookId,
                Pageable.unpaged()
            )
            .filter(user::isInClub)
            .toList();

        for (Club club : clubsReadingBook) {
            final ReadingGoal goal = readingGoalRepository
                .findByClub_IdAndBookId(club.getId(), bookId)
                .orElse(null);

            final var memberFinishedReadingActivity = MemberCompletedReadingActivity
                .builder()
                .bookUser(bookUser)
                .club(club)
                .startDate(goal != null ? goal.getStartDate() : null)
                .endDate(goal != null ? goal.getEndDate() : null)
                .build();

            activityRepository.save(memberFinishedReadingActivity);
        }

        final var userCompletedReadingActivity = UserCompletedReadingActivity
            .builder()
            .bookUser(bookUser)
            .user(getUser(bookUser.getId().getUserId()))
            .build();

        activityRepository.save(userCompletedReadingActivity);
    }

    @Override
    @Transactional
    public BookUserDTO update(BookUserDTO dto) {
        BookUserId id = new BookUserId(dto.bookId(), dto.userId());

        BookUser current = bookUserRepository.findById(id).orElseThrow(
            () -> new BookUserNotFoundException(id)
        );

        final BookUser updated = bookUserRepository.save(
            updateBookUserDTOMapper.partialUpdate(dto, current)
        );

        if (updated.hasFinishedBook()) {
            publishCompletedReadingActivities(updated);
        }

        return bookUserDTOMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void delete(UUID userId, String bookId) {
        BookUserId id = new BookUserId(bookId, userId);
        
        boolean progressExists = bookUserRepository.existsById(id);
        
        if (!progressExists) throw new BookUserNotFoundException(id);
        
        bookUserRepository.deleteById(id);
    }
    
    @Override
    public BookUserDTO findById(UUID userId, String bookId) {
        BookUserId id = new BookUserId(bookId, userId);
        BookUser progress = bookUserRepository.findById(id)
        .orElseThrow(() -> new BookUserNotFoundException(id));
        
        return bookUserDTOMapper.toDTO(progress);
    }

}

package br.upe.booklubapi.app.activities.services;

import br.upe.booklubapi.app.activities.dtos.ActivityDTO;
import br.upe.booklubapi.app.activities.dtos.ClubActivityDTO;
import br.upe.booklubapi.app.activities.dtos.UserActivityDTO;
import br.upe.booklubapi.app.activities.mappers.ActivityDTOMapperResolver;
import br.upe.booklubapi.domain.activities.entities.clubactivities.ClubActivity;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import br.upe.booklubapi.domain.activities.repositories.ClubActivityRepository;
import br.upe.booklubapi.domain.activities.repositories.UserActivityRepository;
import br.upe.booklubapi.utils.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ActivitiesServiceImpl implements ActivitiesService {

    private final ClubActivityRepository clubActivityRepository;

    private final UserActivityRepository userActivityRepository;

    private final ActivityDTOMapperResolver mapper;

    private final UserUtils userUtils;

    @Override
    public PagedModel<ActivityDTO> getActivitiesForUser(
        Pageable pageable,
        Optional<List<ActivityType>> type
    ) {
        final UUID loggedUserId = userUtils.getLoggedUserId();

        final Page<ClubActivity> page;
        if (type.isPresent()) {
            page = clubActivityRepository.findClubActivitiesForUserAndTypeIn(
                loggedUserId, type.get(), pageable
            );
        } else {
            page = clubActivityRepository.findClubActivitiesForUser(loggedUserId, pageable);
        }

        return new PagedModel<>(
            page.map(mapper::toDTO)
        );
    }

    @Override
    public PagedModel<ClubActivityDTO> getClubActivities(
        UUID clubId,
        Pageable pageable,
        Optional<List<ActivityType>> type
    ) {
        if (type.isPresent()) {
            return new PagedModel<>(
                clubActivityRepository.findAllByClubIdAndTypeIn(clubId, type.get(), pageable)
                    .map(activity -> mapper.toDTO(activity, ClubActivityDTO.class))
            );
        }
        return new PagedModel<>(
            clubActivityRepository.findAllByClubId(clubId, pageable)
                .map(activity -> mapper.toDTO(activity, ClubActivityDTO.class))
        );
    }

    @Override
    public PagedModel<UserActivityDTO> getUserActivities(
        UUID userId,
        Pageable pageable,
        Optional<List<ActivityType>> type
    ) {
        if (type.isPresent()) {
            return new PagedModel<>(
                userActivityRepository.findAllByUserIdAndTypeIn(userId, type.get(), pageable)
                    .map(activity -> mapper.toDTO(activity, UserActivityDTO.class))
            );
        }
        return new PagedModel<>(
            userActivityRepository.findAllByUserId(userId, pageable)
                .map(activity -> mapper.toDTO(activity, UserActivityDTO.class))
        );
    }

}

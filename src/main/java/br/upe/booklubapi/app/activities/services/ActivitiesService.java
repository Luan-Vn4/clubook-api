package br.upe.booklubapi.app.activities.services;

import br.upe.booklubapi.app.activities.dtos.ActivityDTO;
import br.upe.booklubapi.app.activities.dtos.ClubActivityDTO;
import br.upe.booklubapi.app.activities.dtos.UserActivityDTO;
import br.upe.booklubapi.domain.activities.entities.enums.ActivityType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivitiesService {

    // TODO A ideia desse método seria buscar as atividades que interessam
    //      àquele usuário, juntando as atividades dos clubes que ele participa
    //      e dos amigos dele
    PagedModel<ActivityDTO> getActivitiesForUser(Pageable pageable, Optional<List<ActivityType>> type);

    PagedModel<ClubActivityDTO> getClubActivities(UUID clubId, Pageable pageable, Optional<List<ActivityType>> type);

    PagedModel<UserActivityDTO> getUserActivities(UUID userId, Pageable pageable, Optional<List<ActivityType>> type);

}

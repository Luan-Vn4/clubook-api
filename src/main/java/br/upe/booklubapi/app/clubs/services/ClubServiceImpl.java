package br.upe.booklubapi.app.clubs.services;

import br.upe.booklubapi.app.notifications.services.NotificationService;
import br.upe.booklubapi.app.clubs.dtos.*;
import br.upe.booklubapi.app.user.dtos.mappers.UserDTOMapper;
import br.upe.booklubapi.domain.clubs.entities.Club;
import br.upe.booklubapi.domain.clubs.entities.QClub;
import br.upe.booklubapi.domain.clubs.exceptions.ClubNotFoundException;
import br.upe.booklubapi.domain.clubs.repositories.ClubRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClubServiceImpl implements ClubService {

    private final CreateClubDTOMapper createClubDTOMapper;

    private final ClubDTOMapper clubDTOMapper;

    private final UserDTOMapper userDTOMapper;

    private final UpdateClubDTOMapper updateClubDTOMapper;  

    private final ClubRepository clubRepository;

    private final NotificationService notificationService;

    private final ClubMediaStorageService clubMediaStorageService;

    private final QClub club = QClub.club;

    @Override
    @Transactional
    public ClubDTO create(CreateClubDTO dto) {
        final Club club = createClubDTOMapper.toEntity(dto);

        final Club created = clubRepository.save(club);

        final String imagePath = clubMediaStorageService.saveClubPicture(
            dto.image(),
            created.getId()
        );

        created.setImageUrl(imagePath);

        clubRepository.save(club);

        notificationService.sendNotification(
                dto.ownerId(), // Pega o ID do dono que veio dentro do DTO
                "Clube criado com sucesso! 🎉",
                "Parabéns! O seu novo clube de leitura '" + created.getName() + "' foi criado e já está pronto para receber membros.",
                "CLUB_CREATED"
        );


        return clubDTOMapper.toDto(created);
    }

    @Override
    @Transactional
    public ClubDTO update(UpdateClubDTO dto, UUID id) {
        Club current = clubRepository.findById(id).orElseThrow(
            () -> new ClubNotFoundException(id)
        );

        final Club updated = updateClubDTOMapper.partialUpdate(dto, current);

        final String path = clubMediaStorageService.saveClubPicture(
            dto.image(),
            updated.getId()
        );

        updated.setImageUrl(path);

        clubRepository.save(current);

        return clubDTOMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        boolean exists = clubRepository.existsById(id);
        if (!exists) throw new ClubNotFoundException(id);
        clubRepository.deleteById(id);
    }

    @Override
    public ClubDTO findById(UUID id) {
        Club club = clubRepository.findById(id).orElseThrow(
            () -> new ClubNotFoundException(id)
        );
        return clubDTOMapper.toDto(club);
    }

    @Override
    public PagedModel<ClubDTO> findAll(Pageable pageable) {
        return new PagedModel<>(
            clubRepository.findAll(pageable)
                .map(clubDTOMapper::toDto)
        );
    }

    @Override
    public PagedModel<ClubDTO> findAll(
        QueryClubDTO queryDTO,
        Pageable pageable
    ) {
        return new PagedModel<>(
            clubRepository.findAll(queryDTO.getQuery(club), pageable)
                .map(clubDTOMapper::toDto)
        );
    }

}

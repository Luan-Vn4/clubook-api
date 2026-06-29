package br.upe.booklubapi.app.user.dtos.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import br.upe.booklubapi.app.user.dtos.UserDTO;
import br.upe.booklubapi.app.user.services.UserMediaStorageService;
import br.upe.booklubapi.domain.users.entities.User;
import br.upe.booklubapi.domain.users.repository.UserRepository;
import lombok.AllArgsConstructor;

@Mapper(
    componentModel=MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy= ReportingPolicy.IGNORE,
    uses=UserDTOMapperHelper.class
)
public interface UserDTOMapper {
    User toEntity(UserDTO userDTO);

    @Mapping(source="user", target="imageUrl", qualifiedByName="imageUrltoAttributes")
    @Mapping(source="id", target="id", qualifiedByName="uuidToString")
    @Mapping(source="username", target="username", qualifiedByName="nullToEmpty")
    @Mapping(source="email", target="email", qualifiedByName="nullToEmpty")
    @Mapping(source="firstName", target="firstName", qualifiedByName="nullToEmpty")
    @Mapping(source="lastName", target="lastName", qualifiedByName="nullToEmpty")
    @Mapping(source="birthDate", target="birthDate", qualifiedByName="nullToEmpty")
    @Mapping(target="totalClubs", expression="java(userDTOMapperHelper.getTotalClubs(user.getId()))")
    UserDTO toDTO(User user);
}

@Component
@AllArgsConstructor
class UserDTOMapperHelper {
    private final UserMediaStorageService userMediaStorageService;

    private final UserRepository userRepository;

    public Integer getTotalClubs(UUID userId) {
        return userRepository.countUserClubs(userId);
    }

    @Named("imageUrltoAttributes")
    public String handleImageUrlMapping(User user) {
        if (!StringUtils.hasText(user.getImage())) {
            return "";
        }
        return userMediaStorageService.getProfilePictureUrl(user.getImage());
    }

    @Named("nullToEmpty")
    public String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    @Named("uuidToString")
    public String uuidToString(UUID id) {
        return id == null ? "" : id.toString();
    }
}
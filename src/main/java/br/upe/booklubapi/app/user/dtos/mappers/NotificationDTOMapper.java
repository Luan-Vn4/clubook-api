package br.upe.booklubapi.app.user.dtos.mappers;

import br.upe.booklubapi.app.user.dtos.NotificationDTO;
import br.upe.booklubapi.domain.notification.entities.Notification;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class NotificationDTOMapper implements Function<Notification, NotificationDTO> {

    @Override
    public NotificationDTO apply(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
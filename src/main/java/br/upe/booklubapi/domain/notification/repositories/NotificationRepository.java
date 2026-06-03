package br.upe.booklubapi.domain.notification.repositories;

import java.util.List;
import java.util.UUID;

import br.upe.booklubapi.domain.core.repositories.CrudRepository;
import br.upe.booklubapi.domain.notification.entities.Notification;

public interface NotificationRepository extends CrudRepository<Notification, UUID> {
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId);

    long countByRecipientIdAndIsReadFalse(UUID recipientId);
}

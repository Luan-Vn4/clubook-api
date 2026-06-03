package br.upe.booklubapi.infra.notifications.repositories;

<<<<<<< HEAD
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.upe.booklubapi.domain.notification.entities.Notification;
import br.upe.booklubapi.domain.notification.repositories.NotificationRepository;

public interface JpaNotificationRepository
        extends NotificationRepository, JpaRepository<Notification, UUID> {

    @Override
    List<Notification> findByRecipientIdOrderByCreatedAtDesc(UUID recipientId);

    @Override
    long countByRecipientIdAndIsReadFalse(UUID recipientId);
}
=======
import br.upe.booklubapi.domain.notification.entities.Notification;
import br.upe.booklubapi.domain.notification.repositories.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaNotificationRepository 
    extends JpaRepository<Notification, UUID>, NotificationRepository {
}
>>>>>>> 2cd68dc20da9c6724737186e3c454150291d3b88

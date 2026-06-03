package br.upe.booklubapi.infra.notifications.repositories;

import br.upe.booklubapi.domain.notification.entities.Notification;
import br.upe.booklubapi.domain.notification.repositories.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaNotificationRepository 
    extends JpaRepository<Notification, UUID>, NotificationRepository {
}
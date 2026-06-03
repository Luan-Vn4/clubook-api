package br.upe.booklubapi.infra.notifications.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.upe.booklubapi.domain.notification.entities.Notification;
import br.upe.booklubapi.domain.notification.repositories.NotificationRepository;

public interface JpaNotificationRepository
        extends JpaRepository<Notification, UUID>, NotificationRepository {
}

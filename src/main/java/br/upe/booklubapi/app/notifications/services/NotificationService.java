package br.upe.booklubapi.app.notifications.services;

import java.util.List;
import java.util.UUID;

import br.upe.booklubapi.app.notifications.dtos.NotificationDTO;

public interface NotificationService {

    void sendNotification(UUID recipientId, String title, String message, String type);

    void markAsRead(UUID notificationId, UUID userId);

    List<NotificationDTO> getUserNotifications(UUID userId);

}

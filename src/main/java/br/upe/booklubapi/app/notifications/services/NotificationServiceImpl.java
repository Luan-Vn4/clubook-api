package br.upe.booklubapi.app.notifications.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.upe.booklubapi.app.notifications.dtos.NotificationDTO;
import br.upe.booklubapi.domain.notification.entities.Notification;
import br.upe.booklubapi.domain.notification.repositories.NotificationRepository;
import br.upe.booklubapi.domain.users.entities.User;
import br.upe.booklubapi.domain.users.repository.UserRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void sendNotification(UUID recipientId, String title, String message, String type) {

        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Notification notification = new Notification();

        notification.setId(UUID.randomUUID());
        notification.setRecipient(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }

    @Transactional
    public void markAsRead(UUID notificationId, UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO: Implement this method
    }

    public List<NotificationDTO> getUserNotifications(UUID userId) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO: Implement this method
    }
}
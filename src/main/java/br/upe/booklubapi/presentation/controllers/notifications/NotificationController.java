package br.upe.booklubapi.presentation.controllers.notifications;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.upe.booklubapi.app.notifications.dtos.NotificationDTO;
import br.upe.booklubapi.app.notifications.services.NotificationService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /** 
     * Returns all notifications for the logged-in user
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotifications(
        @PathVariable UUID userId // We usually get the userID from the url
    ) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    /**
     * Marks a notification as read for the logged-in user
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable UUID notificationId,
            @PathVariable UUID userId) {
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.noContent().build();
    }
}
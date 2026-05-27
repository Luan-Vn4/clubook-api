package br.upe.booklubapi.presentation.controllers.notifications;

import br.upe.booklubapi.domain.notification.dto.NotificationDTO;
import br.upe.booklubapi.domain.notification.service.NotificationServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/me/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Retorna todas as notificações do usuário logado
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getMyNotifications(
            @AuthenticationPrincipal Jwt jwt) { // Baseado no seu Keycloak
        UUID userId = extractUserId(jwt);
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    // Marca uma notificação específica como lida
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable UUID notificationId,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = extractUserId(jwt);
        notificationService.markAsRead(notificationId, userId);
        return ResponseEntity.noContent().build();
    }
}
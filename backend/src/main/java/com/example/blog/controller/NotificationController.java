package com.example.blog.controller;

import com.example.blog.dto.NotificationDTO;
import com.example.blog.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Get all notifications for current user
     * GET /api/notifications
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {
        List<NotificationDTO> notifications = notificationService.getNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread notifications for current user
     * GET /api/notifications/unread
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {
        List<NotificationDTO> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get unread count
     * GET /api/notifications/unread/count
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        long count = notificationService.countUnread();
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Mark notification as read
     * PATCH /api/notifications/{id}/read
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable @NonNull Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }

    /**
     * Mark all notifications as read
     * PATCH /api/notifications/read-all
     */
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    /**
     * Delete notification
     * DELETE /api/notifications/{id}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable @NonNull Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Delete all notifications
     * DELETE /api/notifications
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<Map<String, String>> deleteAllNotifications() {
        notificationService.deleteAllNotifications();
        return ResponseEntity.ok(Map.of("message", "All notifications deleted"));
    }
}

package com.example.blog.service;

import com.example.blog.dto.NotificationDTO;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.mapper.NotificationMapper;
import com.example.blog.model.Notification;
import com.example.blog.model.User;
import com.example.blog.repository.NotificationRepository;
import com.example.blog.security.UserSecurity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final UserSecurity userSecurity;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserService userService,
            UserSecurity userSecurity
    ) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.userSecurity = userSecurity;
    }

    /**
     * Create a notification for a user
     */
    public void createNotification(
            @NonNull Long userId,
            @NonNull String message,
            @NonNull Notification.NotificationType type,
            Long referenceId
    ) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setReferenceId(referenceId);
        notification.setRead(false);

        notificationRepository.save(notification);
    }

    /**
     * Create notifications for multiple users (for post subscribers)
     */
    public void createNotificationsForUsers(
            @NonNull List<Long> userIds,
            @NonNull String message,
            @NonNull Notification.NotificationType type,
            Long referenceId
    ) {
        for (Long userId : userIds) {
            try {
                createNotification(userId, message, type, referenceId);
            } catch (Exception e) {
                // Log error but continue creating other notifications
                System.err.println("Failed to create notification for user " + userId + ": " + e.getMessage());
            }
        }
    }

    /**
     * Get all notifications for current user
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotifications() {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated");
        }

        return notificationRepository.findByUserIdOrderByCreatedAtDesc(currentUserId)
                .stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get unread notifications for current user
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications() {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated");
        }

        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(currentUserId)
                .stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mark notification as read
     */
    public void markAsRead(@NonNull Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        // Check ownership
        if (!userSecurity.isOwnerOrAdmin(notification.getUser().getId())) {
            throw new ForbiddenException("You don't have permission to update this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Mark all notifications as read for current user
     */
    public void markAllAsRead() {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated");
        }

        List<Notification> unreadNotifications = 
            notificationRepository.findByUserIdAndReadFalse(currentUserId);
        
        unreadNotifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Delete notification
     */
    public void deleteNotification(@NonNull Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        // Check ownership
        if (!userSecurity.isOwnerOrAdmin(notification.getUser().getId())) {
            throw new ForbiddenException("You don't have permission to delete this notification");
        }

        notificationRepository.delete(notification);
    }

    /**
     * Delete all notifications for current user
     */
    public void deleteAllNotifications() {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated");
        }

        notificationRepository.deleteByUserId(currentUserId);
    }

    /**
     * Count unread notifications for current user
     */
    @Transactional(readOnly = true)
    public long countUnread() {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            return 0;
        }
        return notificationRepository.countByUserIdAndReadFalse(currentUserId);
    }
}

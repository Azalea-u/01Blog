package com.example.blog.repository;

import com.example.blog.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find all notifications for a user, ordered by creation date (newest first)
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find unread notifications for a user, ordered by creation date (newest first)
     */
    List<Notification> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    /**
     * Find unread notifications for a user (no ordering - for batch updates)
     */
    List<Notification> findByUserIdAndReadFalse(Long userId);

    /**
     * Count unread notifications for a user
     */
    long countByUserIdAndReadFalse(Long userId);

    /**
     * Delete all notifications for a user
     */
    void deleteByUserId(Long userId);
}

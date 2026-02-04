package com.example.blog.mapper;

import com.example.blog.dto.NotificationDTO;
import com.example.blog.model.Notification;

public final class NotificationMapper {

    private NotificationMapper() {}

    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) return null;

        return new NotificationDTO(
            notification.getId(),
            notification.getUser().getId(),
            notification.getMessage(),
            notification.getType(),
            notification.isRead(),
            notification.getReferenceId(),
            notification.getCreatedAt()
        );
    }
}

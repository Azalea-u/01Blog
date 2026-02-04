package com.example.blog.dto;

import com.example.blog.model.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String message;
    private Notification.NotificationType type;
    private boolean read;
    private Long referenceId;
    private OffsetDateTime createdAt;
}

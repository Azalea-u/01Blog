package com.example.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {
    private Long subscriberId;
    private String subscriberUsername;
    private Long targetId;
    private String targetUsername;
    private OffsetDateTime createdAt;
}

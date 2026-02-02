package com.example.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private Long id;
    private String username;
    private OffsetDateTime createdAt;
    private boolean banned;
    private int postCount;
    private int subscriberCount;
    private int subscriptionCount;
}

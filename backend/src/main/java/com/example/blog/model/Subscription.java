package com.example.blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@IdClass(SubscriptionId.class)
public class Subscription {

    @Id
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @Id
    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }

    public User getTarget() { return target; }
    public void setTarget(User target) { this.target = target; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

package com.example.blog.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "subscriptions")
@IdClass(Subscription.SubscriptionId.class)
public class Subscription {

    @Id
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @Id
    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Getters and setters
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }

    public User getTarget() { return target; }
    public void setTarget(User target) { this.target = target; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    // Composite key class defined here for clarity
    public static class SubscriptionId implements Serializable {
        private Long subscriber;
        private Long target;

        public SubscriptionId() {}
        public SubscriptionId(Long subscriber, Long target) {
            this.subscriber = subscriber;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SubscriptionId)) return false;
            SubscriptionId that = (SubscriptionId) o;
            return Objects.equals(subscriber, that.subscriber) &&
                   Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subscriber, target);
        }
    }
}

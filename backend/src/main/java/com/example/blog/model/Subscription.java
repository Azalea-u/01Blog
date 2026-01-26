package com.example.blog.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
    name = "subscriptions",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"subscriber_id", "target_id"})
    },
    indexes = {
        @Index(columnList = "subscriber_id"),
        @Index(columnList = "target_id")
    }
)
@IdClass(Subscription.SubscriptionId.class)
public class Subscription {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "subscriber_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_subscription_subscriber")
    )
    private User subscriber;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "target_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_subscription_target")
    )
    private User target;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /* getters & setters unchanged */
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }

    public User getTarget() { return target; }
    public void setTarget(User target) { this.target = target; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    /* ---------- composite key ---------- */
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
            if (o == null || getClass() != o.getClass()) return false;
            SubscriptionId that = (SubscriptionId) o;
            return Objects.equals(subscriber, that.subscriber)
                && Objects.equals(target, that.target);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subscriber, target);
        }
    }
}

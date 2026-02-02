package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
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
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "subscriber_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_subscription_subscriber")
    )
    @JsonIgnore
    private User subscriber;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "target_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_subscription_target")
    )
    @JsonIgnore
    private User target;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    /* ---------- Composite Key ---------- */
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

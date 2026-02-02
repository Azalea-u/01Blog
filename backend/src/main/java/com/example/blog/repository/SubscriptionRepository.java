package com.example.blog.repository;

import com.example.blog.model.Subscription;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Subscription.SubscriptionId> {

    /**
     * Check if a subscription exists
     */
    boolean existsBySubscriberAndTarget(User subscriber, User target);

    boolean existsBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

    /**
     * Find subscription by subscriber and target
     */
    Optional<Subscription> findBySubscriberAndTarget(User subscriber, User target);

    Optional<Subscription> findBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

    /**
     * Get all users that this user subscribes to (following)
     */
    List<Subscription> findBySubscriber(User subscriber);

    List<Subscription> findBySubscriberId(Long subscriberId);

    /**
     * Get all users that subscribe to this user (followers)
     */
    List<Subscription> findByTarget(User target);

    List<Subscription> findByTargetId(Long targetId);

    /**
     * Count subscribers (followers) of a user
     */
    long countByTargetId(Long targetId);

    /**
     * Count subscriptions (following) of a user
     */
    long countBySubscriberId(Long subscriberId);

    /**
     * Delete subscription
     */
    void deleteBySubscriberIdAndTargetId(Long subscriberId, Long targetId);

    /**
     * Get subscriber IDs for a user (for notifications)
     */
    @Query("SELECT s.subscriber.id FROM Subscription s WHERE s.target.id = :userId")
    List<Long> findSubscriberIdsByTargetId(@Param("userId") Long userId);
}

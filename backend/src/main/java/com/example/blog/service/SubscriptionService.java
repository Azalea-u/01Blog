package com.example.blog.service;

import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Subscription;
import com.example.blog.model.User;
import com.example.blog.repository.SubscriptionRepository;
import com.example.blog.security.UserSecurity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final UserSecurity userSecurity;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            UserService userService,
            UserSecurity userSecurity
    ) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.userSecurity = userSecurity;
    }

    /**
     * Subscribe to a user (follow)
     */
    public void subscribe(@NonNull Long targetUserId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to subscribe");
        }

        // Cannot subscribe to yourself
        if (currentUserId.equals(targetUserId)) {
            throw new BadRequestException("You cannot subscribe to yourself");
        }

        // Check if target user exists
        User targetUser = userService.getUserById(targetUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + targetUserId + " not found"));

        User currentUser = userService.getUserById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Check if already subscribed
        if (subscriptionRepository.existsBySubscriberIdAndTargetId(currentUserId, targetUserId)) {
            throw new BadRequestException("You are already subscribed to this user");
        }

        // Create subscription
        Subscription subscription = new Subscription();
        subscription.setSubscriber(currentUser);
        subscription.setTarget(targetUser);
        subscriptionRepository.save(subscription);
    }

    /**
     * Unsubscribe from a user (unfollow)
     */
    public void unsubscribe(@NonNull Long targetUserId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to unsubscribe");
        }

        // Check if subscription exists
        if (!subscriptionRepository.existsBySubscriberIdAndTargetId(currentUserId, targetUserId)) {
            throw new BadRequestException("You are not subscribed to this user");
        }

        // Delete subscription
        subscriptionRepository.deleteBySubscriberIdAndTargetId(currentUserId, targetUserId);
    }

    /**
     * Check if current user is subscribed to target user
     */
    @Transactional(readOnly = true)
    public boolean isSubscribed(@NonNull Long targetUserId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        return subscriptionRepository.existsBySubscriberIdAndTargetId(currentUserId, targetUserId);
    }

    /**
     * Get users that this user follows (subscriptions)
     */
    @Transactional(readOnly = true)
    public List<User> getSubscriptions(@NonNull Long userId) {
        return subscriptionRepository.findBySubscriberId(userId)
                .stream()
                .map(Subscription::getTarget)
                .toList();
    }

    /**
     * Get users that follow this user (followers/subscribers)
     */
    @Transactional(readOnly = true)
    public List<User> getSubscribers(@NonNull Long userId) {
        return subscriptionRepository.findByTargetId(userId)
                .stream()
                .map(Subscription::getSubscriber)
                .toList();
    }

    /**
     * Count subscribers (followers)
     */
    @Transactional(readOnly = true)
    public long countSubscribers(@NonNull Long userId) {
        return subscriptionRepository.countByTargetId(userId);
    }

    /**
     * Count subscriptions (following)
     */
    @Transactional(readOnly = true)
    public long countSubscriptions(@NonNull Long userId) {
        return subscriptionRepository.countBySubscriberId(userId);
    }

    /**
     * Get subscriber IDs for notifications
     */
    @Transactional(readOnly = true)
    public List<Long> getSubscriberIds(@NonNull Long userId) {
        return subscriptionRepository.findSubscriberIdsByTargetId(userId);
    }
}

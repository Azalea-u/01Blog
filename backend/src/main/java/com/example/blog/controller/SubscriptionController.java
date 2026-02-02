package com.example.blog.controller;

import com.example.blog.dto.ProfileDTO;
import com.example.blog.mapper.UserMapper;
import com.example.blog.model.User;
import com.example.blog.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Subscribe to a user (follow)
     * POST /api/subscriptions/{userId}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{userId}")
    public ResponseEntity<Map<String, String>> subscribe(@PathVariable @NonNull Long userId) {
        subscriptionService.subscribe(userId);
        return ResponseEntity.ok(Map.of("message", "Successfully subscribed"));
    }

    /**
     * Unsubscribe from a user (unfollow)
     * DELETE /api/subscriptions/{userId}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> unsubscribe(@PathVariable @NonNull Long userId) {
        subscriptionService.unsubscribe(userId);
        return ResponseEntity.ok(Map.of("message", "Successfully unsubscribed"));
    }

    /**
     * Check if current user is subscribed to a user
     * GET /api/subscriptions/{userId}/status
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}/status")
    public ResponseEntity<Map<String, Boolean>> checkSubscription(@PathVariable @NonNull Long userId) {
        boolean isSubscribed = subscriptionService.isSubscribed(userId);
        return ResponseEntity.ok(Map.of("subscribed", isSubscribed));
    }

    /**
     * Get users that this user follows (subscriptions)
     * GET /api/subscriptions/{userId}/following
     */
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<ProfileDTO>> getSubscriptions(@PathVariable @NonNull Long userId) {
        List<User> subscriptions = subscriptionService.getSubscriptions(userId);
        List<ProfileDTO> profiles = subscriptions.stream()
                .map(UserMapper::toProfileDTO)
                .toList();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get users that follow this user (followers/subscribers)
     * GET /api/subscriptions/{userId}/followers
     */
    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<ProfileDTO>> getSubscribers(@PathVariable @NonNull Long userId) {
        List<User> subscribers = subscriptionService.getSubscribers(userId);
        List<ProfileDTO> profiles = subscribers.stream()
                .map(UserMapper::toProfileDTO)
                .toList();
        return ResponseEntity.ok(profiles);
    }

    /**
     * Get subscription stats for a user
     * GET /api/subscriptions/{userId}/stats
     */
    @GetMapping("/{userId}/stats")
    public ResponseEntity<Map<String, Long>> getStats(@PathVariable @NonNull Long userId) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("followers", subscriptionService.countSubscribers(userId));
        stats.put("following", subscriptionService.countSubscriptions(userId));
        return ResponseEntity.ok(stats);
    }
}

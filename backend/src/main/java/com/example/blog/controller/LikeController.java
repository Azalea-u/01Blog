package com.example.blog.controller;

import com.example.blog.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Like a post
     * POST /api/posts/{postId}/likes
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Map<String, String>> likePost(@PathVariable @NonNull Long postId) {
        likeService.likePost(postId);
        return ResponseEntity.ok(Map.of("message", "Post liked successfully"));
    }

    /**
     * Unlike a post
     * DELETE /api/posts/{postId}/likes
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<Map<String, String>> unlikePost(@PathVariable @NonNull Long postId) {
        likeService.unlikePost(postId);
        return ResponseEntity.ok(Map.of("message", "Post unliked successfully"));
    }

    /**
     * Check if current user has liked a post
     * GET /api/posts/{postId}/likes/status
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> checkLikeStatus(@PathVariable @NonNull Long postId) {
        boolean isLiked = likeService.isLiked(postId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }

    /**
     * Get like count for a post
     * GET /api/posts/{postId}/likes/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getLikeCount(@PathVariable @NonNull Long postId) {
        long count = likeService.getLikeCount(postId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}

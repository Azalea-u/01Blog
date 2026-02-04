package com.example.blog.service;

import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Like;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.LikeRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.security.UserSecurity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserSecurity userSecurity;

    public LikeService(
            LikeRepository likeRepository,
            PostRepository postRepository,
            UserService userService,
            UserSecurity userSecurity
    ) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.userSecurity = userSecurity;
    }

    /**
     * Like a post
     */
    public void likePost(@NonNull Long postId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to like a post");
        }

        // Check if post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));

        // Check if already liked
        if (likeRepository.existsByUserIdAndPostId(currentUserId, postId)) {
            throw new BadRequestException("You have already liked this post");
        }

        // Get current user
        User currentUser = userService.getUserById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Create like
        Like like = new Like();
        like.setUser(currentUser);
        like.setPost(post);
        likeRepository.save(like);
    }

    /**
     * Unlike a post
     */
    public void unlikePost(@NonNull Long postId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to unlike a post");
        }

        // Check if post exists
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post with ID " + postId + " not found");
        }

        // Check if like exists
        if (!likeRepository.existsByUserIdAndPostId(currentUserId, postId)) {
            throw new BadRequestException("You have not liked this post");
        }

        // Delete like
        likeRepository.deleteByUserIdAndPostId(currentUserId, postId);
    }

    /**
     * Check if current user has liked a post
     */
    @Transactional(readOnly = true)
    public boolean isLiked(@NonNull Long postId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            return false;
        }
        return likeRepository.existsByUserIdAndPostId(currentUserId, postId);
    }

    /**
     * Get like count for a post
     */
    @Transactional(readOnly = true)
    public long getLikeCount(@NonNull Long postId) {
        return likeRepository.countByPostId(postId);
    }
}

package com.example.blog.repository;

import com.example.blog.model.Like;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Like.LikeId> {

    /**
     * Check if a like exists
     */
    boolean existsByUserAndPost(User user, Post post);

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     * Find like by user and post
     */
    Optional<Like> findByUserAndPost(User user, Post post);

    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    /**
     * Get all likes for a post
     */
    List<Like> findByPost(Post post);

    List<Like> findByPostId(Long postId);

    /**
     * Get all likes by a user
     */
    List<Like> findByUser(User user);

    List<Like> findByUserId(Long userId);

    /**
     * Count likes for a post
     */
    long countByPostId(Long postId);

    /**
     * Count likes by a user
     */
    long countByUserId(Long userId);

    /**
     * Delete like
     */
    void deleteByUserIdAndPostId(Long userId, Long postId);
}

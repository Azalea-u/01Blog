package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find all posts by a specific user (for user's block page)
     */
    List<Post> findByUserOrderByCreatedAtDesc(User user);

    /**
     * Find all posts by user ID (for user's block page)
     */
    List<Post> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find posts by user with pagination
     */
    Page<Post> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find posts from users that the given user subscribes to (for feed)
     * This gets posts from people the user follows
     */
    @Query("""
        SELECT p FROM Post p 
        WHERE p.user.id IN (
            SELECT s.target.id FROM Subscription s 
            WHERE s.subscriber.id = :userId
        )
        ORDER BY p.createdAt DESC
    """)
    List<Post> findPostsBySubscriptions(@Param("userId") Long userId);

    /**
     * Find posts from subscriptions with pagination (for feed)
     */
    @Query("""
        SELECT p FROM Post p 
        WHERE p.user.id IN (
            SELECT s.target.id FROM Subscription s 
            WHERE s.subscriber.id = :userId
        )
        ORDER BY p.createdAt DESC
    """)
    Page<Post> findPostsBySubscriptions(@Param("userId") Long userId, Pageable pageable);

    /**
     * Count posts by user
     */
    long countByUserId(Long userId);
}

package com.example.blog.repository;

import com.example.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find all comments for a post, ordered by creation date
     */
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    /**
     * Find all comments by a user
     */
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Count comments on a post
     */
    long countByPostId(Long postId);

    /**
     * Count comments by a user
     */
    long countByUserId(Long userId);
}

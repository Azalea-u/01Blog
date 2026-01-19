package com.example.blog.repository;

import com.example.blog.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Like.LikeId> {

    long countByPostId(Long postId);

    boolean existsByUserIdAndPostId(Long userId, Long postId);
}

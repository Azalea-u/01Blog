package com.example.blog.service;

import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CreateCommentRequest;
import com.example.blog.dto.UpdateCommentRequest;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.security.UserSecurity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final UserSecurity userSecurity;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserService userService,
            UserSecurity userSecurity
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.userSecurity = userSecurity;
    }

    /**
     * Add a comment to a post
     */
    public CommentDTO addComment(@NonNull Long postId, @NonNull CreateCommentRequest request) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to comment");
        }

        // Get post
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + postId + " not found"));

        // Get current user
        User user = userService.getUserById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        // Create comment
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(request.getContent());

        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toDTO(savedComment);
    }

    /**
     * Get all comments for a post
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> getCommentsByPost(@NonNull Long postId) {
        // Verify post exists
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post with ID " + postId + " not found");
        }

        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update a comment (owner only)
     */
    public CommentDTO updateComment(@NonNull Long commentId, @NonNull UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));

        // Check ownership
        if (!userSecurity.isOwnerOrAdmin(comment.getUser().getId())) {
            throw new ForbiddenException("You don't have permission to update this comment");
        }

        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return CommentMapper.toDTO(updatedComment);
    }

    /**
     * Delete a comment (owner or admin)
     */
    public void deleteComment(@NonNull Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment with ID " + commentId + " not found"));

        // Check ownership
        if (!userSecurity.isOwnerOrAdmin(comment.getUser().getId())) {
            throw new ForbiddenException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    /**
     * Get comment count for a post
     */
    @Transactional(readOnly = true)
    public long getCommentCount(@NonNull Long postId) {
        return commentRepository.countByPostId(postId);
    }
}

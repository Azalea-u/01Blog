package com.example.blog.controller;

import com.example.blog.dto.CommentDTO;
import com.example.blog.dto.CreateCommentRequest;
import com.example.blog.dto.UpdateCommentRequest;
import com.example.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Add a comment to a post
     * POST /api/posts/{postId}/comments
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable @NonNull Long postId,
            @RequestBody @Valid CreateCommentRequest request
    ) {
        CommentDTO comment = commentService.addComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    /**
     * Get all comments for a post
     * GET /api/posts/{postId}/comments
     */
    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getComments(@PathVariable @NonNull Long postId) {
        List<CommentDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Update a comment (owner only)
     * PUT /api/comments/{id}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/api/comments/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable @NonNull Long id,
            @RequestBody @Valid UpdateCommentRequest request
    ) {
        CommentDTO updated = commentService.updateComment(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a comment (owner or admin)
     * DELETE /api/comments/{id}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable @NonNull Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

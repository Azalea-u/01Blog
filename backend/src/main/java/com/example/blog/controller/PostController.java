package com.example.blog.controller;

import com.example.blog.dto.CreatePostRequest;
import com.example.blog.dto.PostDTO;
import com.example.blog.dto.UpdatePostRequest;
import com.example.blog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    /* ================= CREATE ================= */

    /**
     * Create a new post with optional media upload
     * POST /api/posts
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> createPost(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "media", required = false) MultipartFile media
    ) {
        CreatePostRequest request = new CreatePostRequest();
        request.setTitle(title);
        request.setContent(content);
        
        PostDTO created = postService.createPost(request, media);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /* ================= READ ================= */

    /**
     * Get all posts (public endpoint - useful for explore/discover)
     * GET /api/posts
     */
    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Get a specific post by ID
     * GET /api/posts/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable @NonNull Long id) {
        PostDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    /**
     * Get all posts by a specific user (for user's block page)
     * GET /api/posts/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable @NonNull Long userId) {
        List<PostDTO> posts = postService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    /**
     * Get feed for current authenticated user (posts from subscriptions)
     * GET /api/posts/feed
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/feed")
    public ResponseEntity<List<PostDTO>> getFeed() {
        List<PostDTO> feed = postService.getFeedForCurrentUser();
        return ResponseEntity.ok(feed);
    }

    /**
     * Get feed with pagination
     * GET /api/posts/feed/page?page=0&size=10
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/feed/page")
    public ResponseEntity<Page<PostDTO>> getFeedPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostDTO> feed = postService.getFeedForCurrentUser(page, size);
        return ResponseEntity.ok(feed);
    }

    /* ================= UPDATE ================= */

    /**
     * Update a post (owner or admin only)
     * PUT /api/posts/{id}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable @NonNull Long id,
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart(value = "media", required = false) MultipartFile media
    ) {
        UpdatePostRequest request = new UpdatePostRequest();
        request.setTitle(title);
        request.setContent(content);
        
        PostDTO updated = postService.updatePost(id, request, media);
        return ResponseEntity.ok(updated);
    }

    /* ================= DELETE ================= */

    /**
     * Delete a post (owner or admin only)
     * DELETE /api/posts/{id}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable @NonNull Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /* ================= ADMIN ENDPOINTS ================= */

    /**
     * Admin can delete any post
     * DELETE /api/posts/{id}/admin
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/admin")
    public ResponseEntity<Void> adminDeletePost(@PathVariable @NonNull Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}

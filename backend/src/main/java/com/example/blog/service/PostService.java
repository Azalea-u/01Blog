package com.example.blog.service;

import com.example.blog.dto.CreatePostRequest;
import com.example.blog.dto.PostDTO;
import com.example.blog.dto.UpdatePostRequest;
import com.example.blog.exception.ForbiddenException;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.mapper.PostMapper;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.PostRepository;
import com.example.blog.security.UserSecurity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final UserSecurity userSecurity;

    public PostService(PostRepository postRepository, UserService userService, UserSecurity userSecurity) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.userSecurity = userSecurity;
    }

    /* ================= CREATE ================= */

    /**
     * Create a new post for the current authenticated user
     */
    public PostDTO createPost(@NonNull CreatePostRequest request) {
        // Get current user
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to create a post");
        }

        User user = userService.getUserById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create post
        Post post = PostMapper.fromCreateRequest(request, user);
        Post savedPost = postRepository.save(post);

        return PostMapper.toDTO(savedPost, currentUserId);
    }

    /* ================= READ ================= */

    /**
     * Get all posts (admin or public feed)
     */
    @Transactional(readOnly = true)
    public List<PostDTO> getAllPosts() {
        Long currentUserId = userSecurity.getCurrentUserId();
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(post -> PostMapper.toDTO(post, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * Get post by ID
     */
    @Transactional(readOnly = true)
    public PostDTO getPostById(@NonNull Long id) {
        Long currentUserId = userSecurity.getCurrentUserId();
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));
        
        return PostMapper.toDTO(post, currentUserId);
    }

    /**
     * Get all posts by a specific user (for user's block page)
     */
    @Transactional(readOnly = true)
    public List<PostDTO> getPostsByUserId(@NonNull Long userId) {
        Long currentUserId = userSecurity.getCurrentUserId();
        
        // Verify user exists
        if (!userService.getUserById(userId).isPresent()) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }

        return postRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(post -> PostMapper.toDTO(post, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * Get posts from users that the current user subscribes to (feed)
     */
    @Transactional(readOnly = true)
    public List<PostDTO> getFeedForCurrentUser() {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to view your feed");
        }

        return postRepository.findPostsBySubscriptions(currentUserId)
                .stream()
                .map(post -> PostMapper.toDTO(post, currentUserId))
                .collect(Collectors.toList());
    }

    /**
     * Get feed with pagination
     */
    @Transactional(readOnly = true)
    public Page<PostDTO> getFeedForCurrentUser(int page, int size) {
        Long currentUserId = userSecurity.getCurrentUserId();
        if (currentUserId == null) {
            throw new ForbiddenException("You must be authenticated to view your feed");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findPostsBySubscriptions(currentUserId, pageable);
        
        return posts.map(post -> PostMapper.toDTO(post, currentUserId));
    }

    /* ================= UPDATE ================= */

    /**
     * Update a post (only owner or admin)
     */
    public PostDTO updatePost(@NonNull Long id, @NonNull UpdatePostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));

        // Check ownership
        if (!userSecurity.isOwnerOrAdmin(post.getUser().getId())) {
            throw new ForbiddenException("You don't have permission to update this post");
        }

        // Update post
        PostMapper.updateFromRequest(post, request);
        Post updatedPost = postRepository.save(post);

        Long currentUserId = userSecurity.getCurrentUserId();
        return PostMapper.toDTO(updatedPost, currentUserId);
    }

    /* ================= DELETE ================= */

    /**
     * Delete a post (only owner or admin)
     */
    public void deletePost(@NonNull Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post with ID " + id + " not found"));

        // Check ownership
        if (!userSecurity.isOwnerOrAdmin(post.getUser().getId())) {
            throw new ForbiddenException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    /* ================= UTILITY ================= */

    /**
     * Count posts by user
     */
    @Transactional(readOnly = true)
    public long countPostsByUser(@NonNull Long userId) {
        return postRepository.countByUserId(userId);
    }
}

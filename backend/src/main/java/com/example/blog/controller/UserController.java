package com.example.blog.controller;

import com.example.blog.dto.*;
import com.example.blog.mapper.UserMapper;
import com.example.blog.model.Role;
import com.example.blog.model.User;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* ================= ADMIN ENDPOINTS ================= */

    /**
     * Get all users (Admin only)
     * GET /api/users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserAdminDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toAdminDTO)
                .toList();
    }

    /**
     * Ban a user (Admin only)
     * PATCH /api/users/{id}/ban
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/ban")
    public ResponseEntity<Void> banUser(@PathVariable @NonNull Long id) {
        userService.banUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Unban a user (Admin only)
     * PATCH /api/users/{id}/unban
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/unban")
    public ResponseEntity<Void> unbanUser(@PathVariable @NonNull Long id) {
        userService.unbanUser(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Update user role (Admin only)
     * PATCH /api/users/{id}/role
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable @NonNull Long id,
            @RequestParam Role role
    ) {
        userService.updateUserRole(id, role);
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a user (Admin only)
     * DELETE /api/users/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /* ================= USER PROFILE ENDPOINTS ================= */

    /**
     * Get user's own profile
     * GET /api/users/me
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        // This will be implemented in AuthController using SecurityContext
        // For now, return 501 Not Implemented
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * Get user by ID (returns full info if self or admin, public profile otherwise)
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable @NonNull Long id) {
        return userService.getUserById(id)
                .map(user -> {
                    // If owner or admin, return full UserDTO
                    // Otherwise return ProfileDTO
                    // For now, return ProfileDTO for everyone
                    return ResponseEntity.ok(UserMapper.toProfileDTO(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get public profile by username
     * GET /api/users/profile/{username}
     */
    @GetMapping("/profile/{username}")
    public ResponseEntity<ProfileDTO> getUserProfile(@PathVariable @NonNull String username) {
        return userService.getUserByUsername(username)
                .map(UserMapper::toProfileDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /* ================= USER UPDATE ENDPOINTS ================= */

    /**
     * Update user profile (owner or admin only)
     * PUT /api/users/{id}
     */
    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable @NonNull Long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(UserMapper.toDTO(updated));
    }

    /**
     * Change password (owner only)
     * PUT /api/users/{id}/password
     */
    @PreAuthorize("@userSecurity.isOwnerOrAdmin(#id)")
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable @NonNull Long id,
            @RequestBody @Valid PasswordChangeRequest request
    ) {
        userService.changePassword(id, request);
        return ResponseEntity.ok().build();
    }
}

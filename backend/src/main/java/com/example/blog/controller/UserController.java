package com.example.blog.controller;

import com.example.blog.dto.UserDTO;
import com.example.blog.dto.UserMapper;
import com.example.blog.model.User;
import com.example.blog.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Get user by ID (admin or self)
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable @NonNull Long id) {
        return userService.getUserById(id)
                .map(UserMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create user (public registration)
    @PostMapping
    public UserDTO createUser(@RequestBody @NonNull User user) {
        User created = userService.createUser(user);
        return UserMapper.toDTO(created);
    }

    // Update user (admin or self)
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable @NonNull Long id, @RequestBody @NonNull User user) {
        User updated = userService.updateUser(id, user);
        return UserMapper.toDTO(updated);
    }

    // Delete user (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

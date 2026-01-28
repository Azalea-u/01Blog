package com.example.blog.controller;

import com.example.blog.dto.UserDTO;
import com.example.blog.dto.UserAdminDTO;
import com.example.blog.mapper.UserMapper;
import com.example.blog.model.User;
import com.example.blog.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ================= ADMIN =================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserAdminDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toAdminDTO)
                .toList();
    }

    // ================= PUBLIC / SELF =================

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable @NonNull Long id) {
        return userService.getUserById(id)
                .map(UserMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= REGISTRATION =================

    @PostMapping
    public UserDTO registerUser(@RequestBody @NonNull User user) {
        // TEMPORARY: replace with RegisterUserDTO later
        User created = userService.createUser(user);
        return UserMapper.toDTO(created);
    }

    // ================= UPDATE =================

    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserDTO updateUser(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull User user
    ) {
        User updated = userService.updateUser(id, user);
        return UserMapper.toDTO(updated);
    }

    // ================= DELETE =================

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NonNull Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

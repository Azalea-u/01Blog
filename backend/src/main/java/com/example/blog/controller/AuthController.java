package com.example.blog.controller;

import com.example.blog.model.User;
import com.example.blog.security.JwtUtil;
import com.example.blog.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Login endpoint
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = Objects.requireNonNull(body.get("username"), "username is required");
        String password = Objects.requireNonNull(body.get("password"), "password is required");

        User user = userService.getUserByUsername( username)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        return Map.of("token", token);
    }

    // Register endpoint
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User created = userService.createUser(user);
        String token = jwtUtil.generateToken(created.getUsername(), created.getRole());
        return Map.of("token", token);
    }
}

package com.example.blog.service;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(@NonNull Long id) {
        return userRepository.findById(id);
    }

    // Get user by username (for login/auth)
    public Optional<User> getUserByUsername(@NonNull String username) {
        return userRepository.findByUsername(username);
    }

    // Create user
    public User createUser(@NonNull User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    // Update user
    public User updateUser(@NonNull Long id, @NonNull User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
            }
            user.setRole(updatedUser.getRole());
            user.setIsBanned(updatedUser.getIsBanned());
            return userRepository.save(user);
        }).orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    }

    // Delete user
    public void deleteUser(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}

package com.example.blog.mapper;

import com.example.blog.dto.ProfileDTO;
import com.example.blog.dto.UserDTO;
import com.example.blog.dto.UserAdminDTO;
import com.example.blog.model.User;

public final class UserMapper {

    private UserMapper() {}

    /* ================= PUBLIC API ================= */

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.isBanned(),
            user.getCreatedAt()
        );
    }

    /* ================= PUBLIC PROFILE (No Email) ================= */

    public static ProfileDTO toProfileDTO(User user) {
        if (user == null) return null;

        return new ProfileDTO(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt(),
            user.isBanned(),
            user.getPosts() != null ? user.getPosts().size() : 0,
            user.getSubscribers() != null ? user.getSubscribers().size() : 0,
            user.getSubscriptions() != null ? user.getSubscriptions().size() : 0
        );
    }

    /* ================= ADMIN API ================= */

    public static UserAdminDTO toAdminDTO(User user) {
        if (user == null) return null;

        return new UserAdminDTO(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole(),
            user.isBanned(),
            user.getCreatedAt(),
            user.getPosts() != null ? user.getPosts().size() : 0,
            user.getReportsReceived() != null ? user.getReportsReceived().size() : 0
        );
    }
}

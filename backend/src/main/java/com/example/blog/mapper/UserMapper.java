package com.example.blog.mapper;

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
            user.getIsBanned(),
            user.getCreatedAt()
        );
    }

    /* ================= ADMIN API ================= */

    public static UserAdminDTO toAdminDTO(User user) {
        if (user == null) return null;

        UserAdminDTO dto = new UserAdminDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setBanned(user.getIsBanned());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
}

package com.example.blog.dto;

import com.example.blog.model.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getIsBanned(),
                user.getCreatedAt()
        );
    }
}

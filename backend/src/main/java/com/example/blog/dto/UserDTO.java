package com.example.blog.dto;

import com.example.blog.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean banned;
    private OffsetDateTime createdAt;
}

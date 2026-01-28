package com.example.blog.dto;

import java.time.OffsetDateTime;

public class UserAdminDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean banned;
    private OffsetDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Boolean getBanned() { return banned; }
    public void setBanned(Boolean banned) { this.banned = banned; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}

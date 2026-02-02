package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(
    name = "users",
    indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "email")
    }
)
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    @JsonIgnore  // Never expose password in JSON
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_banned", nullable = false)
    private boolean banned = false;

    /* ---------- Relations ---------- */

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Like> likes = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(
        mappedBy = "subscriber",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(
        mappedBy = "target",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Subscription> subscribers = new ArrayList<>();

    @OneToMany(
        mappedBy = "reporter",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Report> reportsMade = new ArrayList<>();

    @OneToMany(
        mappedBy = "reportedUser",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<Report> reportsReceived = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }
}


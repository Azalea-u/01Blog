package com.example.blog.model;

import jakarta.persistence.*;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String role = "USER";

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "is_banned", nullable = false)
    private boolean banned = false;

    /* ---------- relations ---------- */

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Post> posts = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Like> likes = new ArrayList<>();

    @OneToMany(
        mappedBy = "user",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(
        mappedBy = "subscriber",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(
        mappedBy = "target",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Subscription> subscribers = new ArrayList<>();

    @OneToMany(
        mappedBy = "reporter",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Report> reportsMade = new ArrayList<>();

    @OneToMany(
        mappedBy = "reportedUser",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<Report> reportsReceived = new ArrayList<>();

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public boolean getIsBanned() { return banned; }
    public void setBanned(boolean banned) { this.banned = banned; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    public List<Like> getLikes() { return likes; }
    public void setLikes(List<Like> likes) { this.likes = likes; }

    public List<Notification> getNotifications() { return notifications; }
    public void setNotifications(List<Notification> notifications) { this.notifications = notifications; }

    public List<Subscription> getSubscriptions() { return subscriptions; }
    public void setSubscriptions(List<Subscription> subscriptions) { this.subscriptions = subscriptions; }

    public List<Subscription> getSubscribers() { return subscribers; }
    public void setSubscribers(List<Subscription> subscribers) { this.subscribers = subscribers; }

    public List<Report> getReportsMade() { return reportsMade; }
    public void setReportsMade(List<Report> reportsMade) { this.reportsMade = reportsMade; }

    public List<Report> getReportsReceived() { return reportsReceived; }
    public void setReportsReceived(List<Report> reportsReceived) { this.reportsReceived = reportsReceived; }
}

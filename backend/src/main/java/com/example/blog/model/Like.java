package com.example.blog.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
    name = "likes",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
    },
    indexes = {
        @Index(columnList = "user_id"),
        @Index(columnList = "post_id")
    }
)
@IdClass(Like.LikeId.class)
public class Like {

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_like_user")
    )
    private User user;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(
        name = "post_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_like_post")
    )
    private Post post;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    /* getters & setters unchanged */
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    /* ---------- composite key ---------- */
    public static class LikeId implements Serializable {

        private Long user;
        private Long post;

        public LikeId() {}

        public LikeId(Long user, Long post) {
            this.user = user;
            this.post = post;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LikeId likeId = (LikeId) o;
            return Objects.equals(user, likeId.user)
                && Objects.equals(post, likeId.post);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, post);
        }
    }
}

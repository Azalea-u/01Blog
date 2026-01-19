package com.example.blog.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(
    name = "likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
@IdClass(Like.LikeId.class)
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    // Getters and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    // Composite key class defined here
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
            if (!(o instanceof LikeId)) return false;
            LikeId likeId = (LikeId) o;
            return Objects.equals(user, likeId.user) &&
                   Objects.equals(post, likeId.post);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, post);
        }
    }
}

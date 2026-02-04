package com.example.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
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
@Getter
@Setter
@NoArgsConstructor
public class Like {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_like_user")
    )
    @JsonIgnore
    private User user;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "post_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_like_post")
    )
    @JsonIgnore
    private Post post;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = OffsetDateTime.now();
        }
    }

    /* ---------- Composite Key ---------- */
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

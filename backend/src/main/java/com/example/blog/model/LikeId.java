package com.example.blog.model;

import java.io.Serializable;

public class LikeId implements Serializable {
    private Long user;
    private Long post;

    public LikeId() {}
    public LikeId(Long user, Long post) {
        this.user = user;
        this.post = post;
    }

    // Getters and setters
    public Long getUser() { return user; }
    public void setUser(Long user) { this.user = user; }
    
    public Long getPost() { return post; }
    public void setPost(Long post) { this.post = post; }
}

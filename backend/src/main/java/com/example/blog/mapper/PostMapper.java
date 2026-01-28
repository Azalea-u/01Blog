package com.example.blog.mapper;

import com.example.blog.dto.CreatePostDTO;
import com.example.blog.dto.PostDTO;
import com.example.blog.model.Post;
import com.example.blog.model.User;

public final class PostMapper {

    private PostMapper() {}

    public static PostDTO toDTO(Post post) {
        if (post == null) return null;

        return new PostDTO(
            post.getId(),
            post.getUser().getId(),
            post.getUser().getUsername(),
            post.getTitle(),
            post.getContent(),
            post.getMediaUrl(),
            post.getMediaType() != null ? post.getMediaType().name() : null,
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }

    public static Post fromCreateDTO(CreatePostDTO dto, User user) {
        Post post = new Post();

        post.setUser(user);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setMediaUrl(dto.getMediaUrl());

        if (dto.getMediaType() != null) {
            post.setMediaType(Post.MediaType.valueOf(dto.getMediaType()));
        }

        return post;
    }
}

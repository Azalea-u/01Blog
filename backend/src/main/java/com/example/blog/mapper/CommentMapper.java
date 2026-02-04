package com.example.blog.mapper;

import com.example.blog.dto.CommentDTO;
import com.example.blog.model.Comment;

public final class CommentMapper {

    private CommentMapper() {}

    public static CommentDTO toDTO(Comment comment) {
        if (comment == null) return null;

        return new CommentDTO(
            comment.getId(),
            comment.getPost().getId(),
            comment.getUser().getId(),
            comment.getUser().getUsername(),
            comment.getContent(),
            comment.getCreatedAt()
        );
    }
}

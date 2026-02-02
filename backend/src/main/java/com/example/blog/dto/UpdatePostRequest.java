package com.example.blog.dto;

import com.example.blog.model.Post;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostRequest {

    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;

    @Size(max = 500, message = "Media URL must not exceed 500 characters")
    private String mediaUrl;

    private Post.MediaType mediaType;
}

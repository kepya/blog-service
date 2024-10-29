package com.kychas.blog_service.dtos;

import com.kychas.blog_service.models.Comment;
import com.kychas.blog_service.models.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
public class BlogPostResponse {
    private Long id;

    private String title;
    private UserResponse author;
    private String content;
    private LocalDateTime createdAt;
    private Set<Comment> comments = new HashSet<>();
    private Set<UserResponse> likedByUsers = new HashSet<>();
}

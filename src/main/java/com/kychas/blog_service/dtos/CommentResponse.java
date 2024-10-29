package com.kychas.blog_service.dtos;

import com.kychas.blog_service.models.BlogPost;
import com.kychas.blog_service.models.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
public class CommentResponse {
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    private UserResponse user;

    private BlogPostResponse post;

    private Set<UserResponse> likedByUsers = new HashSet<>();
}

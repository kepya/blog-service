package com.kychas.blog_service.repositories;

import com.kychas.blog_service.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
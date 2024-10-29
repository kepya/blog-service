package com.kychas.blog_service.repositories;

import com.kychas.blog_service.models.BlogPost;
import com.kychas.blog_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByAuthorOrderByCreatedAtDesc(User author);
    List<BlogPost> findByCommentsUserOrderByCreatedAtDesc(User user);
    List<BlogPost> findByLikedByUsersOrderByCreatedAtDesc(User user);
}
package com.kychas.blog_service.services;

import com.kychas.blog_service.dtos.*;
import com.kychas.blog_service.models.BlogPost;
import com.kychas.blog_service.models.Comment;
import com.kychas.blog_service.models.User;
import com.kychas.blog_service.repositories.BlogPostRepository;
import com.kychas.blog_service.repositories.CommentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogService {
    private final BlogPostRepository blogPostRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public BlogService(BlogPostRepository blogPostRepository,
                       CommentRepository commentRepository,
                       UserService userService) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Transactional
    public BlogPostResponse createPost(Authentication authentication, CreatePostRequest request) {
        User user = userService.getCurrentUser(authentication);

        BlogPost post = new BlogPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(user);
        post.setCreatedAt(LocalDateTime.now());

        BlogPost save = blogPostRepository.save(post);
        return mapBlogPost(save);
    }

    @Transactional
    public BlogPostResponse updatePost(Long postId, UpdatePostRequest request, Authentication authentication) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userService.getCurrentUser(authentication);
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to update this post");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        BlogPost save = blogPostRepository.save(post);

        return mapBlogPost(save);
    }

    @Transactional
    public void deletePost(Long postId, Authentication authentication) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userService.getCurrentUser(authentication);
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to delete this post");
        }

        blogPostRepository.delete(post);
    }

    @Transactional
    public CommentResponse addComment(Long postId, CreateCommentRequest request, Authentication authentication) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userService.getCurrentUser(authentication);

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        Comment save = commentRepository.save(comment);
        return mapComment(save);
    }

    @Transactional
    public void likePost(Long postId, Authentication authentication) {
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = userService.getCurrentUser(authentication);

        if (post.getLikedByUsers().contains(user)) {
            post.getLikedByUsers().remove(user);
            user.getLikedPosts().remove(post);
        } else {
            post.getLikedByUsers().add(user);
            user.getLikedPosts().add(post);
        }

        blogPostRepository.save(post);
    }

    @Transactional
    public void likeComment(Long commentId, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        User user = userService.getCurrentUser(authentication);

        if (comment.getLikedByUsers().contains(user)) {
            comment.getLikedByUsers().remove(user);
        } else {
            comment.getLikedByUsers().add(user);
        }

        commentRepository.save(comment);
    }

    public List<BlogPostResponse> getPostsByUser(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return blogPostRepository.findByAuthorOrderByCreatedAtDesc(user).stream().map(this::mapBlogPost).toList();
    }

    public List<BlogPostResponse> getCommentedPosts(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return blogPostRepository.findByCommentsUserOrderByCreatedAtDesc(user).stream().map(this::mapBlogPost).toList();
    }

    public List<BlogPostResponse> getLikedPosts(Authentication authentication) {
        User user = userService.getCurrentUser(authentication);
        return blogPostRepository.findByLikedByUsersOrderByCreatedAtDesc(user).stream().map(this::mapBlogPost).toList();
    }

    public BlogPostResponse mapBlogPost(BlogPost blogPost) {
        return BlogPostResponse
                .builder()
                .title(blogPost.getTitle())
                .content(blogPost.getContent())
                .author(userService.mapUser(blogPost.getAuthor()))
                .comments(blogPost.getComments())
                .likedByUsers(blogPost.getLikedByUsers().stream().map(userService::mapUser).collect(Collectors.toSet()))
                .id(blogPost.getId())
                .createdAt(blogPost.getCreatedAt())
                .build();
    }

    public CommentResponse mapComment(Comment comment) {
        return CommentResponse
                .builder()
                .user(userService.mapUser(comment.getUser()))
                .content(comment.getContent())
                .post(mapBlogPost(comment.getPost()))
                .likedByUsers(comment.getLikedByUsers().stream().map(userService::mapUser).collect(Collectors.toSet()))
                .id(comment.getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

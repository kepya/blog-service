package com.kychas.blog_service.controllers;

import com.kychas.blog_service.dtos.*;
import com.kychas.blog_service.dtos.BlogPostResponse;
import com.kychas.blog_service.models.BlogPost;
import com.kychas.blog_service.models.Comment;
import com.kychas.blog_service.services.BlogService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin("*")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public ResponseEntity<BlogPostResponse> createPost(@RequestBody CreatePostRequest request,
                                               Authentication authentication) {
        BlogPostResponse post = blogService.createPost(authentication, request);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BlogPostResponse> updatePost(@PathVariable Long postId,
                                               @RequestBody UpdatePostRequest request,
                                               Authentication authentication) {
        BlogPostResponse post = blogService.updatePost(postId, request, authentication);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        Authentication authentication) {
        blogService.deletePost(postId, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(@PathVariable Long postId,
                                                      @RequestBody CreateCommentRequest request,
                                                      Authentication authentication) {
        CommentResponse comment = blogService.addComment(postId, request, authentication);
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/{postId}/likes")
    public ResponseEntity<?> likePost(@PathVariable Long postId,
                                      Authentication authentication) {
        blogService.likePost(postId, authentication);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<?> likeComment(@PathVariable Long commentId,
                                         Authentication authentication) {
        blogService.likeComment(commentId, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/my-posts")
    public ResponseEntity<List<BlogPostResponse>> getMyPosts(Authentication authentication) {
        List<BlogPostResponse> posts = blogService.getPostsByUser(authentication);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/commented-posts")
    public ResponseEntity<List<BlogPostResponse>> getCommentedPosts(Authentication authentication) {
        List<BlogPostResponse> posts = blogService.getCommentedPosts(authentication);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/liked-posts")
    public ResponseEntity<List<BlogPostResponse>> getLikedPosts(Authentication authentication) {
        List<BlogPostResponse> posts = blogService.getLikedPosts(authentication);
        return ResponseEntity.ok(posts);
    }
}

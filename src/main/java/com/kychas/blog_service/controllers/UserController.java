package com.kychas.blog_service.controllers;

import com.kychas.blog_service.dtos.UpdateProfileRequest;
import com.kychas.blog_service.dtos.UserResponse;
import com.kychas.blog_service.models.User;
import com.kychas.blog_service.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        UserResponse user = userService.getProfile(authentication);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request,
                                           Authentication authentication) {
        UserResponse user = userService.updateProfile(authentication, request);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/profile")
    public ResponseEntity<?> deleteProfile(Authentication authentication) {
        userService.deleteProfile(authentication);
        return ResponseEntity.ok().build();
    }
}


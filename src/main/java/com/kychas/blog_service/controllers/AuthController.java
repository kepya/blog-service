package com.kychas.blog_service.controllers;


import com.kychas.blog_service.dtos.JwtResponse;
import com.kychas.blog_service.dtos.LoginRequest;
import com.kychas.blog_service.dtos.RegisterRequest;
import com.kychas.blog_service.dtos.UserResponse;
import com.kychas.blog_service.models.User;
import com.kychas.blog_service.securities.JwtTokenProvider;
import com.kychas.blog_service.services.AuthService;
import com.kychas.blog_service.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        UserResponse user = userService.createUser(registerRequest);
        return ResponseEntity.ok(user);
    }
}


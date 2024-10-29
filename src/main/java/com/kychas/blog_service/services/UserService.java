package com.kychas.blog_service.services;

import com.kychas.blog_service.dtos.RegisterRequest;
import com.kychas.blog_service.dtos.UpdateProfileRequest;
import com.kychas.blog_service.dtos.UserResponse;
import com.kychas.blog_service.models.User;
import com.kychas.blog_service.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getName())) {
            throw new RuntimeException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setName(request.getName());

        User save = userRepository.save(user);
        return mapUser(save);
    }

    public UserResponse getProfile(Authentication authentication) {
        return mapUser(getCurrentUser(authentication));
    }

    public User getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public UserResponse updateProfile(Authentication authentication, UpdateProfileRequest request) {
        User user = getCurrentUser(authentication);

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User save = userRepository.save(user);
        return mapUser(user);
    }

    @Transactional
    public void deleteProfile(Authentication authentication) {
        User user = getCurrentUser(authentication);
        userRepository.delete(user);
    }

    public UserResponse mapUser(User user) {
        return UserResponse
                .builder()
                .email(user.getEmail())
                .name(user.getName())
                .id(user.getId())
                .build();
    }
}

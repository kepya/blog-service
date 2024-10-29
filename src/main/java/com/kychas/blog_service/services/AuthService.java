package com.kychas.blog_service.services;

import com.kychas.blog_service.dtos.LoginRequest;
import com.kychas.blog_service.repositories.UserRepository;
import com.kychas.blog_service.securities.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    public String login(LoginRequest loginRequest) {
        UserDetails userDetailByUsername = getUserDetailByUsername(loginRequest.getUsername());
        if (passwordEncoder.matches(loginRequest.getPassword(), userDetailByUsername.getPassword())) {
            return tokenProvider.generateToken(userDetailByUsername);
        }
        throw new RuntimeException("Aucun utilisateur existe avec ces identifiants");
    }

    UserDetails getUserDetailByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Aucun utilisateur n'existe avec ce username: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetailByUsername(username);
    }
}

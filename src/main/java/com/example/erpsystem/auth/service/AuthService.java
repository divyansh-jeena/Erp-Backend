package com.example.erpsystem.auth.service;

import com.example.erpsystem.auth.dto.AuthResponse;
import com.example.erpsystem.auth.dto.LoginRequest;
import com.example.erpsystem.auth.dto.RegisterRequest;
import com.example.erpsystem.auth.entity.Role;
import com.example.erpsystem.auth.entity.User;
import com.example.erpsystem.auth.repository.UserRepository;
import com.example.erpsystem.shared.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;


    public String register(RegisterRequest request) {

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));


        user.setRole(Role.EMPLOYEE);

        userRepository.save(user);

        return "User registered successfully";
    }


    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateAccessToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}
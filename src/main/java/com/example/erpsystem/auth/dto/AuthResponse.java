package com.example.erpsystem.auth.dto;

public record AuthResponse(
        String token,
        Long id,
        String username,
        String role
) {}
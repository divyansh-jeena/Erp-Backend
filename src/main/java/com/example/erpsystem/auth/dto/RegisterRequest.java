package com.example.erpsystem.auth.dto;
public record RegisterRequest(
        String username,
        String password,
        String role
) {}
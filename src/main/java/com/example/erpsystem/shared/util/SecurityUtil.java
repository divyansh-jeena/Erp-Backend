package com.example.erpsystem.shared.util;

import com.example.erpsystem.auth.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof User user) {
            return user.getId();
        }

        throw new RuntimeException("User not authenticated");
    }
}
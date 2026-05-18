package com.javaapp.finance.service;

import com.javaapp.finance.model.User;
import com.javaapp.finance.security.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUserService {

    public static AuthUser getCurrentAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        return (AuthUser) authentication.getPrincipal();
    }

    public static User getCurrentUser() {
        return getCurrentAuthUser().getUser();
    }

    public static int getCurrentUserId() {
        return getCurrentAuthUser().getUser().getId();
    }
}

package com.gabriel.jwtauthentication.api.security;

import com.gabriel.jwtauthentication.domain.model.User;
import com.gabriel.jwtauthentication.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtilities {

    private final UserService userService;

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getAuthenticatedUserEmail() {
        return getAuth().getName();
    }
}

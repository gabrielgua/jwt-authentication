package com.gabriel.jwtauthentication.api.security;

import com.gabriel.jwtauthentication.domain.model.User;
import com.gabriel.jwtauthentication.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthModel register(User user) {
        var token = tokenService.generateToken(setDefaultClaims(user), user);

        return AuthModel
                .builder()
                .email(user.getEmail())
                .accessToken(token)
                .build();
    }

    public AuthModel authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userService.findByEmail(request.getEmail());
        var token = tokenService.generateToken(setDefaultClaims(user), user);

        return AuthModel
                .builder()
                .email(user.getEmail())
                .accessToken(token)
                .build();
    }

    private Map<String, Object> setDefaultClaims(User user) {
        var claims = new HashMap<String, Object>();
        claims.put("userId", user.getId());

        return claims;
    }

}

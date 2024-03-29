package com.gabriel.jwtauthentication.api.security;

import com.gabriel.jwtauthentication.api.assembler.UserAssembler;
import com.gabriel.jwtauthentication.api.domain.UserRegisterRequest;
import com.gabriel.jwtauthentication.domain.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserAssembler assembler;


    @PostMapping("/login")
    public ResponseEntity<AuthModel> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthModel> register(@RequestBody UserRegisterRequest request) {
        var user = assembler.toEntity(request);
        return ResponseEntity.ok(authService.register(userService.save(user)));

    }


}

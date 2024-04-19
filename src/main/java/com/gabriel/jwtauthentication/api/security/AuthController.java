package com.gabriel.jwtauthentication.api.security;

import com.gabriel.jwtauthentication.api.assembler.UserAssembler;
import com.gabriel.jwtauthentication.api.domain.UserRegisterRequest;
import com.gabriel.jwtauthentication.api.openapi.controller.AuthControllerOpenApi;
import com.gabriel.jwtauthentication.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerOpenApi {

    private final AuthService authService;
    private final UserService userService;
    private final UserAssembler assembler;


    @PostMapping("/login")
    public ResponseEntity<AuthModel> authenticate(@RequestBody @Valid AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthModel> register(@RequestBody @Valid UserRegisterRequest userRequest) {
        var user = assembler.toEntity(userRequest);
        return ResponseEntity.ok(authService.register(userService.save(user)));
    }
}

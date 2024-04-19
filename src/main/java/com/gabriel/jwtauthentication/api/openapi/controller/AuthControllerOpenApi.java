package com.gabriel.jwtauthentication.api.openapi.controller;

import com.gabriel.jwtauthentication.api.domain.UserRegisterRequest;
import com.gabriel.jwtauthentication.api.security.AuthModel;
import com.gabriel.jwtauthentication.api.security.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Manages the authentication and registering of users.")
public interface AuthControllerOpenApi {

    @Operation(summary = "Authenticates a user", description = "Authenticates a user by email and password and returns a valid JWT token and the user's email.")
    ResponseEntity<AuthModel> authenticate(@RequestBody(description = "Authentication request", required = true) AuthRequest request);

    @Operation(summary = "Registers a new user", description = "Registers a new user and returns a valid JWT token and the user's email.")
    ResponseEntity<AuthModel> register(@RequestBody(description = "Representation of new user used for registration only.", required = true) UserRegisterRequest userRequest);
}

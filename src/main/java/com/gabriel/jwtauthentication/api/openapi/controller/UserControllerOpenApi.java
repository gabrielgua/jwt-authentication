package com.gabriel.jwtauthentication.api.openapi.controller;

import com.gabriel.jwtauthentication.api.domain.UserModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User", description = "Contains the endpoint to fetch the authenticated user's info.")
public interface UserControllerOpenApi {
    @Operation(
            summary = "Get authenticated user's info",
            description = "Returns authenticated user's info. Needs a valid JWT token, witch can be granted by authenticating or registering an user.")
    UserModel myInfo();
}

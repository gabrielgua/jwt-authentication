package com.gabriel.jwtauthentication.api.controller;

import com.gabriel.jwtauthentication.api.assembler.UserAssembler;
import com.gabriel.jwtauthentication.api.domain.UserModel;
import com.gabriel.jwtauthentication.api.openapi.controller.UserControllerOpenApi;
import com.gabriel.jwtauthentication.api.security.AuthUtilities;
import com.gabriel.jwtauthentication.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {

    private final UserService userService;
    private final AuthUtilities authUtils;
    private final UserAssembler assembler;

    @GetMapping("/my-info")
    public UserModel myInfo() {
        return assembler.toModel(userService.findByEmail(authUtils.getAuthenticatedUserEmail()));
    }
}

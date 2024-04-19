package com.gabriel.jwtauthentication.api.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @Email
    @NotBlank
    @Schema(example = "john@email.com")
    private String email;

    @NotBlank
    @Schema(example = "myStrongPassword@123")
    private String password;
}

package com.gabriel.jwtauthentication.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank
    @Schema(example = "John Doe")
    private String name;

    @Email
    @NotBlank
    @Schema(example = "johndoe@email.com")
    private String email;

    @NotBlank
    @Schema(example = "myStrongPassword@123")
    private String password;


}


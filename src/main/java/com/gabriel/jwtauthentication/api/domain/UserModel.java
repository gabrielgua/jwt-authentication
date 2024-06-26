package com.gabriel.jwtauthentication.api.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "John Doe")
    private String name;

    @Schema(example = "johndoe@email.com")
    private String email;
}

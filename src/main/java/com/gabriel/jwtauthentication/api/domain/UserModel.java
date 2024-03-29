package com.gabriel.jwtauthentication.api.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    private Long id;
    private String name;
    private String email;
}

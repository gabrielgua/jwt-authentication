package com.gabriel.jwtauthentication.api.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthModel {

    private String email;

    @JsonProperty("access_token")
    private String accessToken;
}

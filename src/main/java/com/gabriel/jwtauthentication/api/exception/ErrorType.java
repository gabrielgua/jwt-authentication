package com.gabriel.jwtauthentication.api.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    GENERIC("/generic-error", "Generic Error"),
    BAD_REQUEST("/bad-request", "Bad request"),
    ACCESS_DENIED("/access-denied", "Access Denied"),
    BAD_CREDENTIALS("/bad-credentials", "Bad Credentials"),
    INVALID_ARGUMENTS("/invalid-arguments", "Invalid Arguments");


    private final String uri;
    private final String title;

    ErrorType(String path, String title) {
        this.title = title;
        this.uri = "https://jwt-authentication.com/api/errors" + path;
    }

}

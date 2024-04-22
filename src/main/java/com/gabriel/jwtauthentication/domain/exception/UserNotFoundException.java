package com.gabriel.jwtauthentication.domain.exception;

public class UserNotFoundException extends GenericException {

    public UserNotFoundException(String email) {
        super(String.format("User not found for email: %s", email));
    }
}

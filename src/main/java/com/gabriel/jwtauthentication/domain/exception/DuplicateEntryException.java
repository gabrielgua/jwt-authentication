package com.gabriel.jwtauthentication.domain.exception;

public class DuplicateEntryException extends GenericException {
    public DuplicateEntryException(String email) {
        super(String.format("The email address '%s' is already registered.", email));
    }

}

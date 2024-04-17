package com.gabriel.jwtauthentication.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Error {

    private int status;
    private OffsetDateTime timestamp;
    private String type;
    private String title;
    private String message;
    private List<ErrorField> fields;

    @Getter
    @Builder
    public static class ErrorField {
        private String name;
        private String message;
    }
}

package com.gabriel.jwtauthentication.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "Error")
public class Error {

    @Schema(example = "400")
    private int status;

    @Schema(example = "2024-04-19T00:00:00-03:00")
    private OffsetDateTime timestamp;

    @Schema(example = "https://jwt-authentication.com/api/errors/bad-request")
    private String type;

    @Schema(example = "Bad Request")
    private String title;

    @Schema(example = "Invalid arguments, please check and try again.")
    private String message;

    @Schema(description = "Error fields")
    private List<ErrorField> fields;

    @Getter
    @Builder
    @Schema(name = "ErrorField")
    public static class ErrorField {

        @Schema(example = "email")
        private String name;

        @Schema(example = "Needs to be a valid email address")
        private String message;
    }
}

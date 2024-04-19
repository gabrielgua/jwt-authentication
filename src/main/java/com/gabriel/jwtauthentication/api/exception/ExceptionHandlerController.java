package com.gabriel.jwtauthentication.api.exception;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.gabriel.jwtauthentication.domain.exception.GenericException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private static final String GENERIC_ERROR_MESSAGE = "Internal server error. Please try again later";
    private static final String INVALID_ARGUMENTS_ERROR_MESSAGE = "Invalid arguments, please check and try again.";

    private Error.ErrorBuilder createErrorBuilder(HttpStatus status, ErrorType type, String message) {
        return Error.builder()
                .message(message)
                .type(type.getUri())
                .title(type.getTitle())
                .status(status.value())
                .timestamp(OffsetDateTime.now());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            body = Error.builder()
                    .status(statusCode.value())
                    .title(HttpStatus.valueOf(statusCode.value()).getReasonPhrase())
                    .message(GENERIC_ERROR_MESSAGE)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        if (body instanceof String) {
            body = Error.builder()
                    .status(statusCode.value())
                    .title((String) body)
                    .message(GENERIC_ERROR_MESSAGE)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        var status = HttpStatus.valueOf(statusCode.value());
        var type = ErrorType.INVALID_ARGUMENTS;

        List<Error.ErrorField> fields = getErrorFields(bindingResult);

        var error = createErrorBuilder(status, type, INVALID_ARGUMENTS_ERROR_MESSAGE)
                .fields(fields)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    private List<Error.ErrorField> getErrorFields(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(error -> {
                   String name = error.getObjectName();
                   String message = error.getDefaultMessage();

                   if (error instanceof FieldError) {
                       name = ((FieldError) error).getField();
                   }

                   return Error.ErrorField.builder()
                           .name(name)
                           .message(message)
                           .build();

                }).collect(Collectors.toList());
    }

    private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        var status = HttpStatus.valueOf(statusCode.value());
        var path = joinPath(ex.getPath());
        var className = ex.getReferringClass().getSimpleName();
        var type = ErrorType.BAD_REQUEST;
        var message = String.format("The '%s' property doesn't exist on type '%s'.", path, className);

        var error = createErrorBuilder(status, type, message)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        var status = HttpStatus.valueOf(statusCode.value());
        var path = joinPath(ex.getPath());
        var className = ex.getTargetType().getSimpleName();
        var type = ErrorType.BAD_REQUEST;
        var message = String.format("The '%s' property received a '%s' value, but was expecting a '%s'.", path, ex.getValue(), className);

        var error = createErrorBuilder(status, type, message)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }


    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) rootCause, headers, statusCode, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBinding((PropertyBindingException) rootCause, headers, statusCode, request);
        }

        var status = HttpStatus.valueOf(statusCode.value());
        var type = ErrorType.BAD_REQUEST;
        var message = "Invalid body request.";

        var error = createErrorBuilder(status, type, message)
                .build();

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleGeneric(GenericException ex, WebRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        var type = ErrorType.GENERIC;


        var error = createErrorBuilder(status, type, ex.getMessage()).build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        var status = HttpStatus.FORBIDDEN;
        var message = "You don't have access to this feature.";
        var type = ErrorType.ACCESS_DENIED;

        var error = createErrorBuilder(status, type, message)
                .build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        var status = HttpStatus.UNAUTHORIZED;
        var type = ErrorType.BAD_CREDENTIALS;
        var message = "Invalid credentials, check and try the login again.";

        var error = createErrorBuilder(status, type, message)
                .build();

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }
}

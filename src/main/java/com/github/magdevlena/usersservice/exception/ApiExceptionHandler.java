package com.github.magdevlena.usersservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ApplicationError handleGenericException(Exception e, HttpServletRequest request) {
        log.error("Unexpected error occurred: " + e.getMessage());
        return createApplicationError(INTERNAL_SERVER_ERROR, request, e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApplicationError handleConstraintViolationException(ConstraintViolationException e,
                                                               HttpServletRequest request) {
        return createApplicationError(BAD_REQUEST, request, e);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ApplicationError handleHttpClientException(HttpClientErrorException e, HttpServletRequest request) {
        return createApplicationError(HttpStatus.resolve(e.getStatusCode().value()), request, e);
    }

    private ApplicationError createApplicationError(HttpStatus httpStatus, HttpServletRequest request, Exception e) {
        final String path = request.getRequestURI();
        log.info("Returning HTTP status: {} for path: {}", httpStatus, path);
        log.debug("Exception details: ", e);
        return ApplicationError.from(httpStatus, path, e.getMessage());
    }

    record ApplicationError(HttpStatusCode status, String path, String message, ZonedDateTime timestamp) {
        static ApplicationError from(HttpStatusCode status, String path, String message) {
            return new ApplicationError(status, path, message, ZonedDateTime.now());
        }
    }
}

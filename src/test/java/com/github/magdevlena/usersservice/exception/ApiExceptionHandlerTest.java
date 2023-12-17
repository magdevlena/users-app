package com.github.magdevlena.usersservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.time.ZonedDateTime;

import static com.github.magdevlena.usersservice.exception.ApiExceptionHandler.ApplicationError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    private static final String REQUEST_PATH = "/request/path";
    private static final String MESSAGE = "some exception message";
    private static final ZonedDateTime NOW = ZonedDateTime.now();

    private static final MockedStatic<ZonedDateTime> ZONED_DATE_TIME_MOCKED_STATIC = mockStatic(ZonedDateTime.class);

    @Mock
    private HttpServletRequest request;

    private final ApiExceptionHandler sut = new ApiExceptionHandler();

    @BeforeEach
    void prepareRequestAndTimeMocks() {
        when(request.getRequestURI()).thenReturn(REQUEST_PATH);
        when(ZonedDateTime.now()).thenReturn(NOW);
    }

    @AfterAll
    static void closeStaticMock() {
        ZONED_DATE_TIME_MOCKED_STATIC.close();
    }

    @Test
    void handleGenericException_ReturnsApplicationErrorWithInternalServerError() {
        // given
        final var exception = new Exception(MESSAGE);

        // when
        final var result = sut.handleGenericException(exception, request);

        // then
        assertEquals(getExpectedApplicationError(INTERNAL_SERVER_ERROR), result);
    }

    @Test
    void handleConstraintViolationException_ReturnsApplicationErrorWithBadRequest() {
        // given
        final var constraintViolationException = mock(ConstraintViolationException.class);
        when(constraintViolationException.getMessage()).thenReturn(MESSAGE);

        // when
        final var result = sut.handleConstraintViolationException(constraintViolationException, request);

        // then
        assertEquals(getExpectedApplicationError(BAD_REQUEST), result);
    }

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {"NOT_FOUND", "UNAUTHORIZED", "REQUEST_TIMEOUT"})
    void handleHttpClientException_ReturnsApplicationErrorWithStatusFromException(HttpStatus status) {
        // given
        final var httpClientErrorException = mock(HttpClientErrorException.class);
        when(httpClientErrorException.getStatusCode()).thenReturn(HttpStatusCode.valueOf(status.value()));
        when(httpClientErrorException.getMessage()).thenReturn(MESSAGE);

        // when
        final var result = sut.handleHttpClientException(httpClientErrorException, request);

        // then
        assertEquals(ResponseEntity.status(status).body(getExpectedApplicationError(status)), result);
    }

    private ApplicationError getExpectedApplicationError(HttpStatus status) {
        return new ApplicationError(status, REQUEST_PATH, MESSAGE, NOW);
    }
}
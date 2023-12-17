package com.github.magdevlena.usersservice.monitoring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RequestCountAspectTest {

    public static final String LOGIN = "login";
    @Mock
    private RequestCountService requestCountService;

    @InjectMocks
    private RequestCountAspect sut;

    @Test
    void incrementRequestsForLogin_WhenIncrementingThrows_DoesNotThrow() {
        // given
        doThrow(new RuntimeException()).when(requestCountService).incrementRequestsForLogin(LOGIN);

        // when-then
        assertDoesNotThrow(() -> sut.incrementRequestsForLogin(LOGIN));
    }
}
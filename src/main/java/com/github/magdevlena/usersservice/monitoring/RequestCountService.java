package com.github.magdevlena.usersservice.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestCountService {

    private final RequestCounter requestsCounter;

    public void incrementRequestsForLogin(String login) {
        final Long incrementedRequestCount = requestsCounter.incrementRequestCount(login);
        log.info("Received #{} request for user with login {}", incrementedRequestCount, login);
    }
}

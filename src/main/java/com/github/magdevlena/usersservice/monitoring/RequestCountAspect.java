package com.github.magdevlena.usersservice.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@EnableAspectJAutoProxy
@EnableAsync
@Aspect
@Component
@RequiredArgsConstructor
public class RequestCountAspect {

    private final RequestCountService requestCountService;

    @Async
    @Before("@annotation(com.github.magdevlena.usersservice.monitoring.CountRequestByLogin) && args(login)")
    public void incrementRequestsForLogin(String login) {
        try {
            requestCountService.incrementRequestsForLogin(login);
        } catch (Exception e) {
            log.warn("Failed to increment request count for login " + login);
            log.debug("Exception details: ", e);
        }
    }
}

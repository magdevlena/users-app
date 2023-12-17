package com.github.magdevlena.usersservice.monitoring;

public interface RequestCounter {
    /**
     * Increments stored request count per specified login.
     *
     * @param login login received in request
     * @return request count after incrementation
     */
    Long incrementRequestCount(String login);
}

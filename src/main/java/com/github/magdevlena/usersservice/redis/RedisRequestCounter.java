package com.github.magdevlena.usersservice.redis;

import com.github.magdevlena.usersservice.monitoring.RequestCounter;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisRequestCounter implements RequestCounter {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Long> valueOps;

    @Override
    public Long incrementRequestCount(String login) {
        return valueOps.increment(login);
    }
}

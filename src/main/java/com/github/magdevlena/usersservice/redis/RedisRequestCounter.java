package com.github.magdevlena.usersservice.redis;

import com.github.magdevlena.usersservice.monitoring.RequestCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRequestCounter implements RequestCounter {

    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    public Long incrementRequestCount(String login) {
        return redisTemplate.opsForValue().increment(login);
    }
}

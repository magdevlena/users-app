package com.github.magdevlena.usersservice.integration;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserApiIntegrationTest {

    public static final String VALID_EXISTING_LOGIN = "octocat";
    public static final String NOT_EXISTING_LOGIN = "this-login-really-does-not-exist";
    public static final String INVALID_LOGIN = "invalid--login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Container
    public static GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redisContainer::getHost);
        registry.add("redis.port", redisContainer::getFirstMappedPort);
    }

    @Test
    @Order(1)
    void getUserAPI_WithFirstApiCall_Returns200AndInitiatesRequestCount() throws Exception {
        // when - then
        mockMvc.perform(get("/users/" + VALID_EXISTING_LOGIN))
                .andExpect(status().isOk());

        // then
        waitForAsyncRequestIncrementation();
        assertEquals(1L, Objects.requireNonNull(redisTemplate.opsForValue().get(VALID_EXISTING_LOGIN)).longValue());
    }

    @Test
    @Order(2)
    void getUserAPI_WithSecondApiCall_Returns200AndIncrementsRequestCount() throws Exception {
        // when - then
        mockMvc.perform(get("/users/" + VALID_EXISTING_LOGIN))
                .andExpect(status().isOk());

        // then
        waitForAsyncRequestIncrementation();
        assertEquals(2L, Objects.requireNonNull(redisTemplate.opsForValue().get(VALID_EXISTING_LOGIN)).longValue());
    }

    @Test
    @Order(3)
    void getUserAPI_WithNotExistingUserLogin_Returns404AndInitiatesRequestCount() throws Exception {
        // when - then
        mockMvc.perform(get("/users/" + NOT_EXISTING_LOGIN))
                .andExpect(status().isNotFound());

        // then
        waitForAsyncRequestIncrementation();
        assertEquals(1L, Objects.requireNonNull(redisTemplate.opsForValue().get(NOT_EXISTING_LOGIN)).longValue());
    }

    @Test
    @Order(4)
    void getUserAPI_WithNotExistingUserLogin_Returns404AndIncrementsRequestCount() throws Exception {
        // when - then
        mockMvc.perform(get("/users/" + NOT_EXISTING_LOGIN))
                .andExpect(status().isNotFound());

        // then
        waitForAsyncRequestIncrementation();
        assertEquals(2L, Objects.requireNonNull(redisTemplate.opsForValue().get(NOT_EXISTING_LOGIN)).longValue());
    }

    @Test
    @Order(5)
    void getUserAPI_WithIncorrectUserLogin_Returns400AndInitiatesRequestCount() throws Exception {
        // when - then
        mockMvc.perform(get("/users/" + INVALID_LOGIN))
                .andExpect(status().isBadRequest());

        // then
        waitForAsyncRequestIncrementation();
        assertEquals(1L, Objects.requireNonNull(redisTemplate.opsForValue().get(INVALID_LOGIN)).longValue());
    }

    @Test
    @Order(6)
    void getUserAPI_WithIncorrectUserLogin_Returns400AndIncrementsRequestCount() throws Exception {
        // when -then
        mockMvc.perform(get("/users/" + INVALID_LOGIN))
                .andExpect(status().isBadRequest());

        // then
        waitForAsyncRequestIncrementation();
        assertEquals(2L, Objects.requireNonNull(redisTemplate.opsForValue().get(INVALID_LOGIN)).longValue());
    }

    @Test
    @Order(7)
    void getUserAPI_WhenRedisUnavailable_Returns200EvenThoughIncrementingRequestCountFailed() throws Exception {
        // given
        redisContainer.stop();

        // when -then
        mockMvc.perform(get("/users/" + VALID_EXISTING_LOGIN))
                .andExpect(status().isOk());
    }

    private void waitForAsyncRequestIncrementation() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1500);
    }
}

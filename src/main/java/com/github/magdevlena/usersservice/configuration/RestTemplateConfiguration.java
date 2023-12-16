package com.github.magdevlena.usersservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfiguration {

    private static final String GITHUB_TOKEN_PROVIDED_LOG =
            "GitHub token provided, application will be using authenticated requests.";
    private static final String MISSING_GITHUB_TOKEN_LOG =
            "GitHub token not provided, application will be using unauthenticated requests"
                    + " - rate limit might be easily exceeded.";
    private final String githubToken;

    public RestTemplateConfiguration(@Value("${github.token:#{null}}") String token) {
        this.githubToken = token;
    }

    @Bean
    @ConditionalOnProperty(name = "github.token")
    public RestTemplate restTemplateWithTokenBearer() {
        log.info(GITHUB_TOKEN_PROVIDED_LOG);
        return new RestTemplateBuilder()
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplateWithoutAuthorization() {
        log.warn(MISSING_GITHUB_TOKEN_LOG);
        return new RestTemplate();
    }
}


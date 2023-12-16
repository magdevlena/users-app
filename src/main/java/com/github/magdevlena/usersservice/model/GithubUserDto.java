package com.github.magdevlena.usersservice.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record GithubUserDto(String id,
                            String login,
                            String name,
                            String type,
                            String avatarUrl,
                            String createdAt,
                            Integer publicRepos,
                            Integer followers) {
}

package com.github.magdevlena.usersservice.model;

public record UserWIthCalculationsDto(String id,
                                      String login,
                                      String name,
                                      String type,
                                      String avatarUrl,
                                      String createdAt,
                                      String calculations) {
}

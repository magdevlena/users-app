package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.GithubUserDto;
import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private final String githubUrl;
    private final RestTemplate restTemplate;
    private final UserMapper mapper;


    public UserService(@Value("${github.url}") String githubUrl, RestTemplate restTemplate, UserMapper mapper) {
        this.githubUrl = githubUrl;
        this.restTemplate = restTemplate;
        this.mapper = mapper;
    }

    UserWithCalculationsDto getUser(String login) {
        final GithubUserDto dto = restTemplate.getForObject(githubUrl + login, GithubUserDto.class);
        return mapper.mapToUserWithCalculations(dto);
    }
}

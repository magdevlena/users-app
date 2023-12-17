package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.GithubUserDto;
import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
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

    public UserWithCalculationsDto getUser(String login) {
        final String url = githubUrl + login;
        log.trace("Performing request to " + url);
        final GithubUserDto dto = restTemplate.getForObject(url, GithubUserDto.class);
        log.trace("Received response from {}, mapping response to user data with calculations.", url);
        return mapper.mapToUserWithCalculations(dto);
    }
}

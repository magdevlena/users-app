package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.GithubUserDto;
import com.github.magdevlena.usersservice.model.UserWIthCalculationsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;

@Validated
@RestController
public class UserController {

    private final String githubUrl;
    private final RestTemplate restTemplate;

    public UserController(@Value("${github-url}") String githubUrl, RestTemplate restTemplate) {
        this.githubUrl = githubUrl;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/users/{login}", produces = "application/json")
    ResponseEntity<UserWIthCalculationsDto> getUser(@PathVariable String login) {
        final GithubUserDto dto = restTemplate.getForObject(githubUrl + login, GithubUserDto.class);
        if (isNull(dto)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(new UserWIthCalculationsDto(dto.id(), dto.login(), "1", "2", "3", "4", "5"));
    }
}

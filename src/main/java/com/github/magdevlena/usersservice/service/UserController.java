package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import com.github.magdevlena.usersservice.validation.GithubLoginRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/users/{login}", produces = "application/json")
    ResponseEntity<UserWithCalculationsDto> getUser(@PathVariable @GithubLoginRequired String login) {
        return ResponseEntity.ok().body(userService.getUser(login));
    }
}

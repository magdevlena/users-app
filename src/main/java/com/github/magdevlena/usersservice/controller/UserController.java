package com.github.magdevlena.usersservice.controller;

import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import com.github.magdevlena.usersservice.monitoring.CountRequestByLogin;
import com.github.magdevlena.usersservice.service.UserService;
import com.github.magdevlena.usersservice.validation.GithubLoginRequired;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@Tag(name = "Users endpoint", description = "REST API for user information.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Gets user information for GitHub login with additional calculations.",
            description = """
                    Gathers information about GitHub user, performs calculations based on followers and public repos
                     count, and returns in JSON response. If it is not possible to perform calculations, remaining
                     information is still returned with a note that calculations result is not available.
                                        
                    Note that user login validation is performed before trying to get user information from GitHub.
                                        
                    Application keeps track of number of performed requests for each login,
                     even if it is invalid or user with such login does not exist.
                    """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request, invalid login"),
            @ApiResponse(responseCode = "404", description = "Not found, user with specified login does not exist"),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error")
    })
    @GetMapping(value = "/users/{login}", produces = "application/json")
    @CountRequestByLogin
    ResponseEntity<UserWithCalculationsDto> getUser(@PathVariable @GithubLoginRequired String login) {
        log.debug("Received request for login {}", login);
        return ResponseEntity.ok().body(userService.getUser(login));
    }
}

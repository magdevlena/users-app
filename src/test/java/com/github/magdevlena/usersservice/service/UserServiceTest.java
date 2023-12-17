package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.GithubUserDto;
import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String LOGIN = "example-login";
    private static final String GITHUB_URL_WITHOUT_TRAILING_SLASH = "someurl.com/users";
    private static final String GITHUB_URL_WITH_TRAILING_SLASH = "someurl.com/users/";
    private static final String GITHUB_URL_WITH_LOGIN = GITHUB_URL_WITH_TRAILING_SLASH + LOGIN;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private UserMapper mapper;
    @Mock
    private GithubUserDto githubUserDto;
    @Mock
    private UserWithCalculationsDto userWithCalculationsDto;

    private static Stream<Arguments> getGithubUrls() {
        return Stream.of(
                arguments(GITHUB_URL_WITH_TRAILING_SLASH),
                arguments(GITHUB_URL_WITHOUT_TRAILING_SLASH)
        );
    }

    @ParameterizedTest
    @MethodSource("getGithubUrls")
    void getUser_Success(String githubUrl) {
        // given
        final UserService sut = new UserService(githubUrl, restTemplate, mapper);
        when(restTemplate.getForObject(GITHUB_URL_WITH_LOGIN, GithubUserDto.class)).thenReturn(githubUserDto);
        when(mapper.mapToUserWithCalculations(githubUserDto)).thenReturn(userWithCalculationsDto);

        // when
        final var result = sut.getUser(LOGIN);

        // then
        assertEquals(userWithCalculationsDto, result);
    }
}
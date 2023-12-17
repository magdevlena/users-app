package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.GithubUserDto;
import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private static final int FOLLOWERS = 5;
    private static final int PUBLIC_REPOS = 9;
    private static final String CALCULATIONS_RESULT = "some calculation result";

    @MockBean
    private CalculationService calculationService;

    @Autowired
    private UserMapper sut;

    @Test
    void mapToUserWithCalculations_ReturnsCorrectlyMappedDto() {
        // given
        final GithubUserDto inputDto = getGithubUserDto(FOLLOWERS, PUBLIC_REPOS);
        final UserWithCalculationsDto outputDto = getExpectedUserWithCalculationsDto(inputDto, CALCULATIONS_RESULT);
        when(calculationService.calculate(FOLLOWERS, PUBLIC_REPOS)).thenReturn(CALCULATIONS_RESULT);

        // when
        final var result = sut.mapToUserWithCalculations(inputDto);

        // then
        assertEquals(outputDto, result);
        verify(calculationService).calculate(FOLLOWERS, PUBLIC_REPOS);
    }

    private GithubUserDto getGithubUserDto(int followers, int publicRepos) {
        return new GithubUserDto("153867232", "magdevlena", null, "User",
                "https://avatars.githubusercontent.com/u/153867232?v=4",
                ZonedDateTime.now().toString(), publicRepos, followers);
    }

    private UserWithCalculationsDto getExpectedUserWithCalculationsDto(GithubUserDto dto,
                                                                       String calculationsResult) {
        return new UserWithCalculationsDto(dto.id(), dto.login(), dto.name(), dto.type(), dto.avatarUrl(),
                dto.createdAt(), calculationsResult);
    }
}
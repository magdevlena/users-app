package com.github.magdevlena.usersservice.service;

import com.github.magdevlena.usersservice.model.GithubUserDto;
import com.github.magdevlena.usersservice.model.UserWithCalculationsDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = CalculationService.class)
public abstract class UserMapper {

    @Autowired
    private CalculationService calculationService;

    @Mapping(target = "calculations", source = "githubUserDto")
    abstract UserWithCalculationsDto mapToUserWithCalculations(GithubUserDto githubUserDto);

    String mapCalculations(GithubUserDto githubUserDto) {
        return calculationService.calculate(githubUserDto.followers(), githubUserDto.publicRepos());
    }
}

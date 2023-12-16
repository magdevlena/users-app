package com.github.magdevlena.usersservice.validation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GithubLoginValidatorTest {

    private final GithubLoginValidator sut = new GithubLoginValidator();

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "endsWith-",
            "-startsWith",
            "double--hyphen",
            "invalid%char",
            "inval!d",
            "inv@lid",
            "too-long-username-that-contains-40-chars"
    })
    void isValid_WithIncorrectLogin_ReturnsFalse(String login) {
        // when - then
        assertFalse(sut.isValid(login, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "simple",
            "with-hyphen",
            "with-multiple-hyphens",
            "with-all-1234567890-digits",
            "0-starts-with-digit",
            "ends-with-digit-0",
            "username-that-contains-maximum-39-chars",
            "a",
            "1",
            "withUpperCaseLetters"
    })
    void isValid_WithValidLogin_ReturnsTrue(String login) {
        // when - then
        assertTrue(sut.isValid(login, null));
    }
}
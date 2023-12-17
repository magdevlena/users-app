package com.github.magdevlena.usersservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
public class GithubLoginValidator implements ConstraintValidator<GithubLoginRequired, String> {

    private static final Pattern PATTERN = Pattern.compile("^\\p{Alnum}(?:\\p{Alnum}|-(?=\\p{Alnum})){0,38}$");

    /**
     * Validates that GitHub login is correct:
     * - contains only alphanumeric characters or single hyphens,
     * - does not begin or end with a hyphen
     * - has between 1 and 39 characters (inclusive)
     *
     * @param login   GitHub login (username)
     * @param context context in which the constraint is evaluated
     * @return true if login is correct, false otherwise
     */
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        log.trace("Validating login " + login);
        final boolean isValid = Optional.ofNullable(login)
                .map(GithubLoginValidator::matchesPattern)
                .orElse(false);

        if (!isValid) {
            log.debug("Invalid GitHub login: {}", login);
        }

        return isValid;
    }

    private static boolean matchesPattern(String login) {
        return PATTERN.matcher(login).matches();
    }
}

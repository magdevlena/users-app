package com.github.magdevlena.usersservice.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.math.RoundingMode;

import static com.github.magdevlena.usersservice.service.CalculationService.FAILED_TO_CALCULATE_RESULT;
import static com.github.magdevlena.usersservice.service.CalculationService.NOT_APPLICABLE_RESULT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationServiceTest {

    @Getter
    @RequiredArgsConstructor
    private enum TestCase {
        DEFAULT_SETTINGS_POSITIVE_CASE_1(2, RoundingMode.HALF_UP, 2, 1, "9.00"),
        DEFAULT_SETTINGS_POSITIVE_CASE_2(2, RoundingMode.HALF_UP, 12, 1, "1.50"),
        DEFAULT_SETTINGS_POSITIVE_CASE_3(2, RoundingMode.HALF_UP, 9, 1, "2.01"),
        MIN_POSSIBLE_RESULT(38, RoundingMode.HALF_UP, Integer.MAX_VALUE, 0, "0.00000000558793545029495630892690099260"),
        MAX_POSSIBLE_RESULT(0, RoundingMode.HALF_UP, 1, Integer.MAX_VALUE, "12884901894"),
        CORNER_CASE(28, RoundingMode.HALF_EVEN, Integer.MAX_VALUE, Integer.MAX_VALUE, "6.0000000055879354503734457705"),
        INTEGER_RESULT_WITH_MORE_THAN_6_FOLLOWERS(0, RoundingMode.FLOOR, 18, 1, "0"),
        INTEGER_RESULT_WITH_LESS_THAN_6_FOLLOWERS(0, RoundingMode.FLOOR, 4, 1, "3"),
        ROUND_FLOOR(2, RoundingMode.FLOOR, 9, 1, "1.98"),
        ROUND_CEILING(2, RoundingMode.CEILING, 18, 1, "1.02"),
        ROUND_UNNECESSARY(2, RoundingMode.UNNECESSARY, 3, 1, "6.00"),
        ROUND_UNNECESSARY_INSUFFICIENT_SCALE(2, RoundingMode.UNNECESSARY, 9, 1, FAILED_TO_CALCULATE_RESULT),
        NO_FOLLOWERS(2, RoundingMode.HALF_UP, 0, 1, NOT_APPLICABLE_RESULT);

        private final int scale;
        private final RoundingMode roundingMode;
        private final int followers;
        private final int publicRepos;
        private final String result;

        CalculationService prepareSut() {
            return new CalculationService(scale, roundingMode);
        }
    }

    @ParameterizedTest
    @EnumSource(TestCase.class)
    void calculate_ReturnsExpected(TestCase testCase) {
        // given
        final CalculationService sut = testCase.prepareSut();

        // when - then
        assertEquals(testCase.result, sut.calculate(testCase.followers, testCase.publicRepos));
    }
}
package com.github.magdevlena.usersservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class CalculationService {

    static final String NOT_APPLICABLE_RESULT = "N/A (user has no followers)";
    static final String FAILED_TO_CALCULATE_RESULT = "N/A (calculations failed)";
    private static final BigDecimal SIX = new BigDecimal(6);
    private static final BigDecimal TWO = new BigDecimal(2);

    private final int scale;
    private final RoundingMode roundingMode;

    public CalculationService(@Value("${calculations.scale:2}") int scale,
                              @Value("${calculations.rounding-mode:HALF_UP}") RoundingMode roundingMode) {
        this.scale = scale;
        this.roundingMode = roundingMode;
    }

    public String calculate(int followers, int publicRepos) {
        if (followers == 0) {
            log.trace("Calculations not applicable, user has no followers.");
            return NOT_APPLICABLE_RESULT;
        }

        try {
            log.trace("Performing calculations with {} followers and {} public repos.", followers, publicRepos);
            return calculate(BigDecimal.valueOf(followers), BigDecimal.valueOf(publicRepos)).toPlainString();
        } catch (ArithmeticException e) {
            log.debug("Unexpected arithmetic exception occurred during calculations.", e);
            return FAILED_TO_CALCULATE_RESULT;
        }
    }

    private BigDecimal calculate(BigDecimal followers, BigDecimal publicRepos) {
        return SIX.divide(followers, scale, roundingMode)
                .multiply(TWO.add(publicRepos));
    }
}

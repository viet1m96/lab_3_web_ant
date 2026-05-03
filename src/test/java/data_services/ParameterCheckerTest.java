package data_services;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParametersCheckerTest {

    private final ParametersChecker checker = new ParametersChecker();

    private static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    @Test
    void pointInsideSquareShouldBeHit() {
        assertTrue(checker.checkParams(bd("-2"), bd("-1"), bd("4")));
    }

    @Test
    void pointOutsideSquareByYShouldBeMiss() {
        assertFalse(checker.checkParams(bd("-2"), bd("-3"), bd("4")));
    }

    @Test
    void pointInsideCircleShouldBeHit() {
        assertTrue(checker.checkParams(bd("3"), bd("-2"), bd("4")));
    }

    @Test
    void pointOutsideCircleShouldBeMiss() {
        assertFalse(checker.checkParams(bd("4"), bd("-3"), bd("4")));
    }

    @Test
    void pointInsideTriangleShouldBeHit() {
        assertTrue(checker.checkParams(bd("-1"), bd("1"), bd("4")));
    }

    @Test
    void pointOutsideTriangleShouldBeMiss() {
        assertFalse(checker.checkParams(bd("-2"), bd("1"), bd("4")));
    }

    @Test
    void originShouldBeHit() {
        assertTrue(checker.checkParams(bd("0"), bd("0"), bd("4")));
    }
}
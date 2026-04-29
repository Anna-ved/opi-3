package by.rozze.weblab3.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AreaCheckServiceTest {

    @ParameterizedTest
    @CsvSource({
            "-1.2, 0.8, 4.0, true",
            "-3.99, 3.5, 4.0, true",
            "-4.0, 4.0, 4.0, true",
            "-0.5, -3.4, 4.0, true",
            "-2.0, -2.0, 4.0, true",
            "-0.1, -3.8, 4.0, true",
            "0.5, 0.5, 4.0, true",
            "1.999, 0.0, 4.0, true",
            "1.2, 1.5, 4.0, true",
            "0.0, 0.0, 4.0, true",

            "-4.1, 2.0, 4.0, false",
            "-2.0, 4.1, 4.0, false",
            "-1.0, -3.6, 4.0, false",
            "-3.0, -2.0, 4.0, false",
            "2.1, 0.0, 4.0, false",
            "1.5, 1.5, 4.0, false",
            "1.0, -0.1, 4.0, false",
            "5.0, 5.0, 4.0, false",

            "1.0, 1.0, 0.0, false",
            "1.0, 1.0, -2.0, false"
    })
    void checkHitTest(double x, double y, double r, boolean expected) {
        assertEquals(expected, AreaCheckService.checkHit(x, y, r));
    }
    @Test
    void invalidNumbersTest() {
        assertFalse(AreaCheckService.checkHit(Double.NaN, 1.0, 4.0));
        assertFalse(AreaCheckService.checkHit(1.0, Double.NaN, 4.0));
        assertFalse(AreaCheckService.checkHit(1.0, 1.0, Double.NaN));
        assertFalse(AreaCheckService.checkHit(Double.POSITIVE_INFINITY, 1.0, 4.0));
        assertFalse(AreaCheckService.checkHit(1.0, Double.NEGATIVE_INFINITY, 4.0));
        assertFalse(AreaCheckService.checkHit(1.0, 1.0, Double.POSITIVE_INFINITY));
    }
}
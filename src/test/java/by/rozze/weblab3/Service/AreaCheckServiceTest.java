package by.rozze.weblab3.Service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AreaCheckServiceTest {
    @Test
    void pointInsideRectangleShouldBeHit() {
        assertTrue(AreaCheckService.checkHit(-1.0, 1.0, 2.0));
    }

    @Test
    void pointOutsideAreaShouldBeMiss() {
        assertFalse(AreaCheckService.checkHit(2.0, 2.0, 2.0));
    }
}

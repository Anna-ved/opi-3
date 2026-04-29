package by.rozze.weblab3.Service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class AreaCheckService {
    public static boolean checkHit(double x, double y, double r) {
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(r) ||
                Double.isInfinite(x) || Double.isInfinite(y) || Double.isInfinite(r) ||
                r <= 0) {
            return false;
        }

        return isRect(x, y, r) || isTriangle(x, y, r) || isCircle(x, y, r);
    }

    private static boolean isTriangle(double x, double y, double r) {
        return x <= 0.0 && x >= -r && y <= 0.0 && y >= -r && (y >= -x - r);
    }

    private static boolean isRect(double x, double y, double r) {
        return x <= 0.0 && y >= 0.0 && x >= -r && y <= r;
    }

    private static boolean isCircle(double x, double y, double r) {
        return x >= 0.0 && y >= 0.0 && x * x + y * y <= r / 2.0 * r / 2.0;
    }
}

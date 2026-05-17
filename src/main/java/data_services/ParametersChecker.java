package data_services;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

@ApplicationScoped
public class ParametersChecker {
    public boolean checkParams(BigDecimal x, BigDecimal y, BigDecimal R) {
        return checkSquare(x, y, R) || checkCircle(x, y, R) || checkTriangle(x, y, R);
    }

    private boolean checkSquare(BigDecimal x, BigDecimal y, BigDecimal R) {
        return x.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(BigDecimal.ZERO) <= 0
                && x.compareTo(R.negate()) >= 0
                && y.compareTo(R.negate()) >= 0;
    }

    private boolean checkCircle(BigDecimal x, BigDecimal y, BigDecimal R) {
        return x.compareTo(BigDecimal.ZERO) >= 0
                && y.compareTo(BigDecimal.ZERO) <= 0
                && x.multiply(x).add(y.multiply(y)).compareTo(R.multiply(R)) <= 0;
    }

    private boolean checkTriangle(BigDecimal x, BigDecimal y, BigDecimal R) {
        return x.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(BigDecimal.ZERO) >= 0
                && y.subtract(x).compareTo(R) <= 0;
    }
}
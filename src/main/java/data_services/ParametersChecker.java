package data_services;

import jakarta.enterprise.context.ApplicationScoped;

import java.math.BigDecimal;

/**
 * Checks whether a point belongs to the area defined by the laboratory task.
 * <p>
 * The area consists of three parts: a rectangle in the third quadrant, a circle
 * sector in the fourth quadrant, and a triangle in the second quadrant. All
 * calculations are performed with {@link BigDecimal} values to avoid precision
 * loss while comparing user input with the area borders.
 *
 * @author viet1m96
 * @version 1.0
 */
@ApplicationScoped
public class ParametersChecker {
    /**
     * Checks whether the point {@code (x, y)} is inside at least one part of the
     * target area for the specified radius {@code R}.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @param R radius value used to define the area borders
     * @return {@code true} if the point is inside the target area; {@code false} otherwise
     */
    public boolean checkParams(BigDecimal x, BigDecimal y, BigDecimal R) {
        return checkSquare(x, y, R) || checkCircle(x, y, R) || checkTriangle(x, y, R);
    }

    /**
     * Checks the rectangular part of the target area.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @param R radius value used to define the rectangle borders
     * @return {@code true} if the point is inside the rectangle
     */
    private boolean checkSquare(BigDecimal x, BigDecimal y, BigDecimal R) {
        return x.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(R.multiply(BigDecimal.valueOf(-1)).divide(BigDecimal.valueOf(2))) >= 0
                && x.compareTo(R.multiply(BigDecimal.valueOf(-1))) >= 0;
    }

    /**
     * Checks the circular part of the target area.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @param R radius of the circle
     * @return {@code true} if the point is inside the circle sector
     */
    private boolean checkCircle(BigDecimal x, BigDecimal y, BigDecimal R) {
        return x.compareTo(BigDecimal.ZERO) >= 0
                && y.compareTo(BigDecimal.ZERO) <= 0
                && (x.multiply(x).add(y.multiply(y)).compareTo(R.multiply(R)) <= 0);
    }

    /**
     * Checks the triangular part of the target area.
     *
     * @param x x-coordinate of the point
     * @param y y-coordinate of the point
     * @param R radius value used to define the triangle border
     * @return {@code true} if the point is inside the triangle
     */
    private boolean checkTriangle(BigDecimal x, BigDecimal y, BigDecimal R) {
        return x.compareTo(BigDecimal.ZERO) <= 0
                && y.compareTo(BigDecimal.ZERO) >= 0
                && (y.subtract(x.multiply(BigDecimal.valueOf(2))).compareTo(R) <= 0);
    }
}

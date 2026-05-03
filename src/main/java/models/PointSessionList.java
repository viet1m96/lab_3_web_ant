package models;

import data_services.JsonService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Session-scoped storage of points submitted during the current user session.
 * <p>
 * The bean keeps unique coordinate pairs in insertion order and exposes them as
 * JSON for drawing points on the client-side graph.
 *
 * @author viet1m96
 * @version 1.0
 */
@Named("pointSession")
@SessionScoped
public class PointSessionList implements Serializable {
    /**
     * Serialization identifier for the session-scoped bean.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Service used to serialize stored points to JSON.
     */
    @Inject
    private JsonService jsonService;

    /**
     * Lightweight data transfer object containing only point coordinates.
     */
    @AllArgsConstructor
    @Getter
    @Setter
    public static final class PointDTO {
        /**
         * Point x-coordinate.
         */
        public final BigDecimal x;

        /**
         * Point y-coordinate.
         */
        public final BigDecimal y;

        /**
         * Compares coordinate pairs using {@link BigDecimal#compareTo(BigDecimal)}
         * so that values with different scale but equal numeric value are treated
         * as equal.
         *
         * @param o object to compare with this DTO
         * @return {@code true} if both objects represent the same coordinates
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PointDTO other)) return false;
            return this.x.compareTo(other.getX()) == 0 && this.y.compareTo(other.getY()) == 0;
        }

        /**
         * Calculates hash code from both coordinates.
         *
         * @return hash code for this coordinate pair
         */
        @Override public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    /**
     * Ordered set of unique points submitted in the current session.
     */
    private final LinkedHashSet<PointDTO> points = new LinkedHashSet<>();

    /**
     * Adds a coordinate pair to the session set.
     *
     * @param x x-coordinate to add
     * @param y y-coordinate to add
     */
    public synchronized void add(BigDecimal x, BigDecimal y) {
        points.add(new PointDTO(x, y));
    }

    /**
     * Returns all stored session points as a JSON string.
     *
     * @return JSON representation of stored points
     */
    public synchronized String getJson() {
        return jsonService.toJson(points);
    }
}

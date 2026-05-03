package models;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Session-scoped bean that stores point parameters submitted by the user.
 * <p>
 * The bean is exposed to JSF pages under the name {@code paramsBean}. Lombok
 * generates the constructors, getters, and setters used by JSF and tests.
 *
 * @author viet1m96
 * @version 1.0
 */
@Named("paramsBean")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParamsBean implements Serializable {
    /**
     * Selected x-coordinate.
     */
    private BigDecimal x;

    /**
     * Selected y-coordinate.
     */
    private BigDecimal y;

    /**
     * Selected radius value.
     */
    private BigDecimal R;
}

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

@Named("paramsBean")
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParamsBean implements Serializable {
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal R;

}


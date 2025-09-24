package models;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "session_results")
@Setter
@Getter
public class Point implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "jsessionid", nullable = false)
    private String sessionId;

    @Column(name = "x", nullable = false)
    private BigDecimal x;

    @Column(name = "y", nullable = false)
    private BigDecimal y;

    @Column(name = "r", nullable = false)
    private BigDecimal R;

    @Column(name = "hit", nullable = false)
    private boolean hit;

    @Column(name = "calculation_time", nullable = false)
    private Double calTime;

    @Column(name = "released_time", nullable = false)
    private LocalDateTime releaseTime;

}
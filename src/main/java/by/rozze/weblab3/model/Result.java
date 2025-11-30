package by.rozze.weblab3.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name="area_results")
@NamedQueries({
        @NamedQuery(name="Result.findAll", query = "SELECT r FROM Result r ORDER BY r.timestamp DESC"),
        @NamedQuery(name="Result.clearAll", query = "DELETE FROM Result")
})
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="x_value",  nullable=false)
    private double x;
    @Column(name="y_value",  nullable=false)
    private double y;
    @Column(name="r_value",  nullable=false)
    private double r;
    @Column(name="timestamp_value",  nullable=false)
    private LocalDateTime timestamp;
    @Column(name="hit_value",  nullable=false)
    private boolean hit;

    public Result(Long id, double x, double y, double r, LocalDateTime timestamp, boolean hit) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.r = r;
        this.timestamp = timestamp;
        this.hit = hit;
    }

    public Result() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public String getTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}


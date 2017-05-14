package helpers;

import java.util.ArrayList;
import java.util.List;

public class PotentialPoint {

    private Double x;
    private Double y;

    private Boolean isObstacle;
    private Double value;

    public PotentialPoint() {
        x = 0.0;
        y = 0.0;

        isObstacle = false;
        value = 0.0;
    }

    public PotentialPoint(Double x, Double y, Boolean isObstacle, Double value) {
        this.x = x;
        this.y = y;
        this.isObstacle = isObstacle;
        this.value = value;
    }

    public PotentialPoint(Double x, Double y) {
        this.x = x;
        this.y = y;
        isObstacle = false;
        value = 0.0;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Boolean getObstacle() {
        return isObstacle;
    }

    public Double getValue() {
        return value;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public void setObstacle(Boolean obstacle) {
        isObstacle = obstacle;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}


package helpers;

import java.util.ArrayList;
import java.util.List;

public class Obstacle {

    private List<MyPair> myPairs;

    public Obstacle() {
        this.myPairs = new ArrayList<>();
    }

    public Obstacle addNewObstaclePoint(double x, double y) {
        this.myPairs.add(new MyPair(x, y));

        return this;
    }

    public static class MyPair {
        private double x;
        private double y;

        public MyPair(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}

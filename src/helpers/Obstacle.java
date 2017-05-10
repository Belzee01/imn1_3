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

    public List<MyPair> getMyPairs() {
        return myPairs;
    }
}


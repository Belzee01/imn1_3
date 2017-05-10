package helpers;

public class BoundingBox {

    private MyPair xRange;
    private MyPair yRange;

    public BoundingBox(MyPair xRange, MyPair yRange) {
        this.xRange = xRange;
        this.yRange = yRange;
    }

    public MyPair getxRange() {
        return xRange;
    }

    public MyPair getyRange() {
        return yRange;
    }
}

package service;

import helpers.BoundingBox;
import helpers.MatrixBuilder.IntegerMatrix;
import helpers.MyPair;
import helpers.Obstacle;

import java.util.ArrayList;
import java.util.List;

import static helpers.MatrixBuilder.DoubleMatrix;

public class MatrixSpace {

    private IntegerMatrix regionMatrix;
    private DoubleMatrix potentialMatrix;

    private BoundingBox box;
    private Double jump;

    private List<Obstacle> obstacles;

    public MatrixSpace(IntegerMatrix integerMatrix, DoubleMatrix doubleMatrix, BoundingBox box, Double jump) {
        this.regionMatrix = integerMatrix;
        this.potentialMatrix = doubleMatrix;
        this.jump = jump;
        this.obstacles = new ArrayList<>();
        this.box = box;
    }

    public MatrixSpace addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
        evaluateObstacleRegions(obstacle);
        return this;
    }

    private void evaluateObstacleRegions(Obstacle obstacle) {

        int numberOfObstacles = obstacle.getMyPairs().size();
        List<MyPair> pairs = obstacle.getMyPairs();

        for (int i = 0; i < numberOfObstacles; i++) {
            if ((pairs.get(i).getX() - pairs.get(i + 1).getX()) < 0.0) {
                for (double x = pairs.get(i).getX(); x < pairs.get(i + 1).getX(); x += jump) {
                    int indexX = getIndexX(x);
                    int indexY = getIndexY(pairs.get(i).getY());
                    this.regionMatrix.setValue(indexX, indexY, 1);
                }
            } else if ((pairs.get(i).getX() - pairs.get(i + 1).getX()) > 0.0) {
                for (double x = pairs.get(i).getX(); x < pairs.get(i + 1).getX(); x -= jump) {
                    int indexX = getIndexX(x);
                    int indexY = getIndexY(pairs.get(i).getY());
                    this.regionMatrix.setValue(indexX, indexY, 1);
                }
            } else if ((pairs.get(i).getY() - pairs.get(i + 1).getY()) < 0.0) {
                for (double y = pairs.get(i).getY(); y < pairs.get(i + 1).getY(); y += jump) {
                    int indexX = getIndexX(pairs.get(i).getX());
                    int indexY = getIndexY(y);
                    this.regionMatrix.setValue(indexX, indexY, 1);
                }
            } else if ((pairs.get(i).getY() - pairs.get(i + 1).getY()) > 0.0) {
                for (double y = pairs.get(i).getY(); y < pairs.get(i + 1).getY(); y -= jump) {
                    int indexX = getIndexX(pairs.get(i).getX());
                    int indexY = getIndexY(y);
                    this.regionMatrix.setValue(indexX, indexY, 1);
                }
            }
        }

    }

    private int getIndexX(double x) {
        return 0;
    }

    private int getIndexY(double y) {
        return 0;
    }

    public IntegerMatrix getIntegerMatrix() {
        return regionMatrix;
    }

    public DoubleMatrix getDoubleMatrix() {
        return potentialMatrix;
    }
}

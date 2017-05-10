package service;

import helpers.BoundingBox;
import helpers.MatrixBuilder.IntegerMatrix;
import helpers.MatrixBuilder.PotentialMatrix;
import helpers.MyPair;
import helpers.Obstacle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixSpace {

    private IntegerMatrix regionMatrix;
    private PotentialMatrix potentialMatrix;

    private BoundingBox box;
    private Double jump;

    private Integer xEdgeLength;
    private Integer yEdgeLength;

    private List<Obstacle> obstacles;

    public MatrixSpace(IntegerMatrix integerMatrix, PotentialMatrix doubleMatrix, BoundingBox box, Double jump) {
        this.regionMatrix = integerMatrix;
        this.potentialMatrix = doubleMatrix;
        this.jump = jump;
        this.obstacles = new ArrayList<>();
        this.box = box;

        this.xEdgeLength = (int) ((box.getxRange().getY() - box.getxRange().getX()) / jump)+1;
        this.yEdgeLength = (int) ((box.getyRange().getY() - box.getyRange().getX()) / jump)+1;
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
        evaluateObstacleRegions();
        printRegions();
        putRegionsInPotentialMatrix();
    }

    private void printRegions() {
        for (Integer[] integers:this.regionMatrix.getMatrix()) {
            System.out.println(Arrays.toString(integers));
        }
    }

    private void evaluateObstacleRegions() {
        boolean isInObstacleRegion = false;

        for (int i = 0; i < yEdgeLength-1; i++) {
            for (int j = 0; j < xEdgeLength-1; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 1 && !isInObstacleRegion) {
                    isInObstacleRegion = true;
                }
                if (this.regionMatrix.getMatrix()[i][j+1] == 1 && isInObstacleRegion) {
                    this.regionMatrix.getMatrix()[i][j] = 1;
                    isInObstacleRegion = false;
                    j++;
                }

                if (this.regionMatrix.getMatrix()[i][j+1] == 0 && isInObstacleRegion) {
                    this.regionMatrix.getMatrix()[i][j] = 1;
                }
            }
        }
    }

    private void putRegionsInPotentialMatrix() {
        for (int i = 0; i < yEdgeLength; i++) {
            for (int j = 0; j < xEdgeLength; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 1) {
                    this.potentialMatrix.setPotentialPointIsObstacle(i, j,true);
                }
            }
        }
    }

    //TODO
    private int getIndexX(double x) {
        return (int) ((x + this.box.getxRange().getY()) / this.jump);
    }

    //TODO
    private int getIndexY(double y) {
        return (int) ((y + this.box.getyRange().getY()) / this.jump);
    }

    public IntegerMatrix getIntegerMatrix() {
        return regionMatrix;
    }

    public PotentialMatrix getDoubleMatrix() {
        return potentialMatrix;
    }
}

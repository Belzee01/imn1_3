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

    private Integer rows;
    private Integer columns;

    private List<Obstacle> obstacles;

    public MatrixSpace(IntegerMatrix integerMatrix, PotentialMatrix doubleMatrix, BoundingBox box, Double jump) {
        this.regionMatrix = integerMatrix;
        this.potentialMatrix = doubleMatrix;
        this.jump = jump;
        this.obstacles = new ArrayList<>();
        this.box = box;

        this.columns = (int) ((box.getxRange().getY() - box.getxRange().getX()) / jump)+1;
        this.rows = (int) ((box.getyRange().getY() - box.getyRange().getX()) / jump)+1;
    }

    public MatrixSpace addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
        evaluateObstacleRegions(obstacle);
        return this;
    }

    private void evaluateObstacleRegions(Obstacle obstacle) {

        int numberOfObstacles = obstacle.getMyPairs().size();
        List<MyPair> pairs = obstacle.getMyPairs();

        for (int i = 0; i < numberOfObstacles-1; i++) {
            if ((pairs.get(i).getX() - pairs.get(i + 1).getX()) < 0.0) {
                for (double x = pairs.get(i).getX(); x < pairs.get(i + 1).getX(); x += jump) {
                    int indexX = getIndexX(x);
                    int indexY = getIndexY(pairs.get(i).getY());
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            } else if ((pairs.get(i).getX() - pairs.get(i + 1).getX()) > 0.0) {
                for (double x = pairs.get(i).getX(); x > pairs.get(i + 1).getX(); x -= jump) {
                    int indexX = getIndexX(x);
                    int indexY = getIndexY(pairs.get(i).getY());
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            } else if ((pairs.get(i).getY() - pairs.get(i + 1).getY()) < 0.0) {
                for (double y = pairs.get(i).getY(); y < pairs.get(i + 1).getY(); y += jump) {
                    int indexX = getIndexX(pairs.get(i).getX());
                    int indexY = getIndexY(y);
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            } else if ((pairs.get(i).getY() - pairs.get(i + 1).getY()) > 0.0) {
                for (double y = pairs.get(i).getY(); y > pairs.get(i + 1).getY(); y -= jump) {
                    int indexX = getIndexX(pairs.get(i).getX());
                    int indexY = getIndexY(y);
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            }
        }
        evaluateObstacleRegions();
        putRegionsInPotentialMatrix();
    }

    private void printRegions() {
        for (Integer[] integers:this.regionMatrix.getMatrix()) {
            System.out.println(Arrays.toString(integers));
        }
    }

    private void evaluateObstacleRegions() {
        for (int i = 0; i < rows; i++) {
            boolean isIn = false;
            for (int j = 1; j < columns-1; j++) {
                if (isIn) {
                    this.regionMatrix.getMatrix()[i][j] = 1;
                }
                if (this.regionMatrix.getMatrix()[i][j] == 0 && this.regionMatrix.getMatrix()[i][j-1] == 1) {
                    isIn = true;
                    this.regionMatrix.getMatrix()[i][j] = 1;
                }
                if (this.regionMatrix.getMatrix()[i][j+1] == 0 && this.regionMatrix.getMatrix()[i][j] == 1 && isIn) {
                    isIn = false;
                }
            }
        }
    }

    private void putRegionsInPotentialMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 1) {
                    this.potentialMatrix.setPotentialPointIsObstacle(i, j,true);
                }
            }
        }
    }

    //TODO
    private int getIndexX(double x) {
        return (int) ((x) / this.jump);
    }

    //TODO
    private int getIndexY(double y) {
        return rows - (int) ((y) / this.jump);
    }

    public IntegerMatrix getIntegerMatrix() {
        return regionMatrix;
    }

    public PotentialMatrix getDoubleMatrix() {
        return potentialMatrix;
    }
}

package service;

import helpers.BoundingBox;
import helpers.MatrixBuilder.IntegerMatrix;
import helpers.MatrixBuilder.PotentialMatrix;
import helpers.MyPair;
import helpers.Obstacle;
import helpers.PotentialPoint;

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

        this.columns = (int) ((box.getxRange().getY() - box.getxRange().getX()) / jump) + 1;
        this.rows = (int) ((box.getyRange().getY() - box.getyRange().getX()) / jump) + 1;

        evaluateXY();
    }

    public MatrixSpace addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
        evaluateObstacleRegions(obstacle);
        return this;
    }

    private void evaluateXY() {
        PotentialPoint[][] potentialPoints = potentialMatrix.getMatrix();
        for (int i = rows-1; i > -1; i--) {
            for (int j = 0; j < columns; j++) {
                potentialPoints[i][j].setX((j+1)*jump);
                potentialPoints[i][j].setY(((rows-i))*jump);
            }
        }

        potentialMatrix.setMatrix(potentialPoints);
    }

    private void evaluateObstacleRegions(Obstacle obstacle) {

        int numberOfObstacles = obstacle.getMyPairs().size();
        List<MyPair> pairs = obstacle.getMyPairs();

        for (int i = 0; i < numberOfObstacles - 1; i++) {
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

    private void evaluateObstacleRegions() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 0 && checkIfFourNeighbors(this.regionMatrix.getMatrix(), i, j)) {
                    this.regionMatrix.getMatrix()[i][j] = 1;
                }
            }
        }
    }

    private  boolean checkIfFourNeighbors(Integer[][] array, int x, int y) {
        int count = 0;
        for (int i = x+1; i < array.length; i++) { // przeglad w dol
            if (array[i][y] == 1){
                count++;
                break;
            }
        }
        for (int i = x-1; i >-1; i--) { // przeglad w gore
            if (array[i][y] == 1){
                count++;
                break;
            }
        }
        for (int i = y+1; i < array[0].length; i++) { // przeglad w prawo
            if (array[x][i] == 1) {
                count++;
                break;
            }
        }
        for (int i = y-1; i > -1; i--) { // przeglad w prawo
            if (array[x][i] == 1){
                count++;
                break;
            }
        }

        return count == 4;
    }

    private void printArray() {
        for (Integer[] i : this.regionMatrix.getMatrix()) {
            System.out.println(Arrays.toString(i));
        }
    }

    private void putRegionsInPotentialMatrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 1) {
                    this.potentialMatrix.setPotentialPointIsObstacle(i, j-1, true);
                }
            }
        }
    }

    private int getIndexX(double x) {
        return (int) ((x) / this.jump);
    }

    private int getIndexY(double y) {
        return rows - (int) ((y) / this.jump);
    }

    public IntegerMatrix getIntegerMatrix() {
        return regionMatrix;
    }

    public PotentialMatrix getDoubleMatrix() {
        return potentialMatrix;
    }

    public Double getJump() {
        return jump;
    }

    public BoundingBox getBox() {
        return box;
    }

    public void setRegionMatrix(IntegerMatrix regionMatrix) {
        this.regionMatrix = regionMatrix;
    }

    public void setPotentialMatrix(PotentialMatrix potentialMatrix) {
        this.potentialMatrix = potentialMatrix;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getColumns() {
        return columns;
    }
}

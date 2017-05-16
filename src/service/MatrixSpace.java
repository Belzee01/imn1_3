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

        this.columns = (int) ((box.getxRange().getY() - box.getxRange().getX()) / jump)+1;
        this.rows = (int) ((box.getyRange().getY() - box.getyRange().getX()) / jump)+1;

        evaluateXY();
    }

    public MatrixSpace addObstacle(Obstacle obstacle) {
        evaluateObstacleRegions(obstacle);
        System.out.println("Added obstacle");
        return this;
    }

    private void evaluateXY() {
        PotentialPoint[][] potentialPoints = potentialMatrix.getMatrix();
        for (int i = rows-1; i > -1; i--) {
            for (int j = 0; j < columns; j++) {
                potentialPoints[i][j].setX((j)*jump);  // for wariant A +1.0
                potentialPoints[i][j].setY((((rows-i)-1)*jump)); // for wariant A +0.0
            }
        }

        potentialMatrix.setMatrix(potentialPoints);
    }

    private void evaluateObstacleRegions(Obstacle obstacle) {

        System.out.println("Calculating obstacles");
        int numberOfObstacles = obstacle.getMyPairs().size();
        List<MyPair> pairs = obstacle.getMyPairs();

        for (int i = 0; i < numberOfObstacles - 1; i++) {
            if ((pairs.get(i).getX() - pairs.get(i + 1).getX()) < 0.0) {
                for (double x = pairs.get(i).getX()/jump; x < pairs.get(i + 1).getX()/jump; x += 1.0) {
                    int indexX = (int) x;
                    int indexY = getIndexY(pairs.get(i).getY());
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            } else if ((pairs.get(i).getX() - pairs.get(i + 1).getX()) > 0.0) {
                for (double x = pairs.get(i).getX()/jump; x > pairs.get(i + 1).getX()/jump; x -= 1.0) {
                    int indexX = (int) x;
                    int indexY = getIndexY(pairs.get(i).getY());
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            } else if ((pairs.get(i).getY() - pairs.get(i + 1).getY()) < 0.0) {
                for (double y = pairs.get(i).getY()/jump; y < pairs.get(i + 1).getY()/jump; y += 1.0) {
                    int indexX = getIndexX(pairs.get(i).getX());
                    int indexY = (int) (rows - y) - 1; //getIndexY(y);
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            } else if ((pairs.get(i).getY() - pairs.get(i + 1).getY()) > 0.0) {
                for (double y = pairs.get(i).getY()/jump; y > pairs.get(i + 1).getY()/jump; y -= 1.0) {
                    int indexX = getIndexX(pairs.get(i).getX());
                    int indexY = (int) (rows - y) - 1;
                    this.regionMatrix.setValue(indexY, indexX, 1);
                }
            }
        }
        System.out.println("Finished calculating obstacles");
        fillObstacles();
        putRegionsInPotentialMatrix();
    }

    private void fillObstacles() {
        System.out.println("Filling obstacles");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 0 && checkIfFourNeighbors(this.regionMatrix.getMatrix(), i, j)) {
                    this.regionMatrix.getMatrix()[i][j] = 1;
                }
            }
        }
        System.out.println("Filled obstacles");
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
        System.out.println("\n\n\n\n\n\n");
    }

    private void putRegionsInPotentialMatrix() {
        System.out.println("Setting obstacle flag");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.regionMatrix.getMatrix()[i][j] == 1) {
                    this.potentialMatrix.setPotentialPointIsObstacle(i, j, true); //for wariant A y-1
                }
            }
        }
        System.out.println("All obstacle flags set");
    }

    private int getIndexX(double x) {
        return (int) Math.round(((x) / this.jump));
    }

    private int getIndexY(double y) {
        return rows - (int) ((y) / this.jump) -1; // for wariant A +0.0
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

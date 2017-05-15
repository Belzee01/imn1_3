package service;

import helpers.IterationIntegralContainer;
import helpers.Obstacle;
import helpers.PotentialPoint;

public class WariantB {
    enum TYPE {
        NEUMANN,
        DIRCHLET,
        NONE
    }

    private MatrixSpace matrixSpace;

    private IterationIntegralContainer iterationIntegralContainer;

    private Obstacle obstaclePoints;

    private TYPE type;

    private Double omega;

    public WariantB(MatrixSpace matrixSpace, Double omega) {
        this.matrixSpace = matrixSpace;

        this.omega = omega;
    }

    public WariantB(MatrixSpace matrixSpace, Obstacle obstaclePoints, Double omega) {
        this.matrixSpace = matrixSpace;
        this.obstaclePoints = obstaclePoints;

        this.omega = omega;
    }

    public void evaluateInitialEdgeValues() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i <=  50.0/matrixSpace.getJump(); i++) { //lewy a
            potentialPoints[matrixSpace.getRows() - i][0].setValue(
                    1.0 * (i - (50.0/matrixSpace.getJump()))
            );
        }

        for (int i = (int) (150.0/matrixSpace.getJump()); i <=  200.0/matrixSpace.getJump(); i++) { //prawy b
            potentialPoints[matrixSpace.getRows() - i][matrixSpace.getColumns()-1].setValue(
                    1.0 * (i - (150.0/matrixSpace.getJump()))
            );
        }

        //dolny
        for (int i = 0; i <=  300.0/matrixSpace.getJump(); i++) { //dolny
            potentialPoints[matrixSpace.getRows()-1][i].setValue(
                    potentialPoints[0][0].getValue()
            );
        }
        for (int i = 0; i <=  150.0/matrixSpace.getJump(); i++) { //dolny
            potentialPoints[matrixSpace.getRows()-i-1][(int) (300.0/matrixSpace.getJump())].setValue(
                    potentialPoints[0][0].getValue()
            );
        }
        for (int i = (int) (300.0/matrixSpace.getJump()); i <=  400.0/matrixSpace.getJump(); i++) { //dolny
            potentialPoints[(int) (matrixSpace.getRows()-150.0/matrixSpace.getJump())][i].setValue(
                    potentialPoints[0][0].getValue()
            );
        }

        //gorny
        for (int i = 0; i <=  100.0/matrixSpace.getJump(); i++) { //dolny
            potentialPoints[(int) (matrixSpace.getRows() - 50.0/matrixSpace.getJump())][i].setValue(
                    potentialPoints[(int) (50.0/matrixSpace.getJump())][0].getValue()
            );
        }
        for (int i = (int) (50.0/matrixSpace.getJump()); i <=  200.0/matrixSpace.getJump(); i++) { //dolny
            potentialPoints[matrixSpace.getRows()-i-1][(int) (100.0/matrixSpace.getJump())].setValue(
                    potentialPoints[(int) (50.0/matrixSpace.getJump())][0].getValue()
            );
        }
        for (int i = (int) (100.0/matrixSpace.getJump()); i <=  400.0/matrixSpace.getJump(); i++) { //dolny
            potentialPoints[0][i].setValue(
                    potentialPoints[(int) (50.0/matrixSpace.getJump())][0].getValue()
            );
        }
    }

    public void overRelaxation() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 1; i < potentialPoints.length - 1; i++) {
            for (int j = 1; j < potentialPoints[0].length - 1; j++) {
                if (!matrixSpace.getDoubleMatrix().getMatrix()[i][j].getObstacle()) {
                    Double value = (1.0 - omega)*potentialPoints[i][j].getValue() + omega*(potentialPoints[i - 1][j].getValue() + potentialPoints[i][j - 1].getValue() +
                            potentialPoints[i + 1][j].getValue() + potentialPoints[i][j + 1].getValue()) / 4.0;
                    potentialPoints[i][j].setValue(value);
                }
            }
        }
    }
}

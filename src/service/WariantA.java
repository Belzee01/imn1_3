package service;

import helpers.PotentialPoint;

public class WariantA {

    private MatrixSpace matrixSpace;

    public WariantA(MatrixSpace matrixSpace) {
        this.matrixSpace = matrixSpace;

        evaluateInitialEdges();
    }

    private void evaluateInitialEdges() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i < potentialPoints.length; i++) {
            for (int j = 0; j < potentialPoints[0].length ; j++) {
                if (j == 0) { // lewy brzeg
                    potentialPoints[i][j].setValue(1.0 * potentialPoints[potentialPoints.length -1 - i][j].getY());
                }
                if (j == potentialPoints[0].length-1) { // prawy brzeg
                    potentialPoints[i][j].setValue(1.0 * potentialPoints[potentialPoints.length -1 - i][j].getY());
                }

                Double potentialMinMax = potentialPoints[0][0].getValue();
                Double potentialMinMin = potentialPoints[potentialPoints.length-1][0].getValue();

                // gorny i dolny brzeg
                if (potentialPoints[i][j].getObstacle()) {
                    if (i != potentialPoints.length-1) {
                        potentialPoints[i][j].setValue(potentialMinMax);
                    } else {
                        potentialPoints[i][j].setValue(potentialMinMin);
                    }
                }
            }
        }
        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);
    }

    private void calculatePotential() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 1; i < potentialPoints.length - 1; i++) {
            for (int j = 1; j < potentialPoints[0].length - 1; j++) {
                Double value = (potentialPoints[i - 1][j].getValue() + potentialPoints[i][j - 1].getValue() +
                        potentialPoints[i + 1][j].getValue() + potentialPoints[i][j + 1].getValue()) / 4.0;
                potentialPoints[i][j].setValue(value);
            }
        }
    }

    public MatrixSpace getMatrixSpace() {
        return matrixSpace;
    }
}

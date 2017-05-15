package service;

import helpers.IterationIntegralContainer;
import helpers.Obstacle;
import helpers.PotentialPoint;

public class WariantA {

    enum TYPE {
        NEUMANN,
        DIRCHLET,
        NONE
    }

    private MatrixSpace matrixSpace;

    private IterationIntegralContainer iterationIntegralContainer;

    private Obstacle obstaclePoints;

    private TYPE type;

    // Dla zadania 1
    public WariantA(MatrixSpace matrixSpace) {
        this.matrixSpace = matrixSpace;
        this.iterationIntegralContainer = new IterationIntegralContainer();
        this.type = TYPE.NONE;

        evaluateInitialEdges();
    }

    //Dla zadania 2
    public WariantA(MatrixSpace matrixSpace, Obstacle obstaclePoints) {
        this.matrixSpace = matrixSpace;
        this.iterationIntegralContainer = new IterationIntegralContainer();
        this.obstaclePoints = obstaclePoints;
        this.type = TYPE.NEUMANN;

        evaluateInitialEdgesVonNuemann();
    }

    private void evaluateInitialEdges() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int j = 0; j < potentialPoints[0].length; j++) {
            potentialPoints[0][j].setObstacle(true);
            potentialPoints[potentialPoints.length - 1][j].setObstacle(true);

        }

        for (int i = 0; i < potentialPoints.length; i++) {
            for (int j = 0; j < potentialPoints[0].length; j++) {
                if (j == 0) { // lewy brzeg
                    potentialPoints[i][j].setValue(1.0 * potentialPoints[i][j].getY());
                }
                if (j == potentialPoints[0].length - 1) { // prawy brzeg
                    potentialPoints[i][j].setValue(1.0 * potentialPoints[i][j].getY());
                }

                Double potentialMinMax = potentialPoints[0][0].getValue();
                Double potentialMinMin = potentialPoints[potentialPoints.length-1][0].getValue();

                // gorny i dolny brzeg
                if (potentialPoints[i][j].getObstacle()) {
                    if (i != potentialPoints.length - 1) {
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
        if (type == TYPE.NEUMANN) {
            evaluateEdgePotential();
        }

        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 1; i < potentialPoints.length - 1; i++) {
            for (int j = 1; j < potentialPoints[0].length - 1; j++) {
                if (!matrixSpace.getDoubleMatrix().getMatrix()[i][j].getObstacle()) {
                    Double value = (potentialPoints[i - 1][j].getValue() + potentialPoints[i][j - 1].getValue() +
                            potentialPoints[i + 1][j].getValue() + potentialPoints[i][j + 1].getValue()) / 4.0;
                    potentialPoints[i][j].setValue(value);
                }
            }
        }
    }

    private Double calculateIntegralAtIteration() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        Double a = 0.0;

        for (int i = 1; i < potentialPoints.length - 1; i++) {
            for (int j = 1; j < potentialPoints[0].length - 1; j++) {
                if (!matrixSpace.getDoubleMatrix().getMatrix()[i][j].getObstacle()) {
                    a += Math.pow(potentialPoints[i + 1][j].getValue() - potentialPoints[i - 1][j].getValue(), 2.0) +
                            Math.pow(potentialPoints[i][j + 1].getValue() - potentialPoints[i][j - 1].getValue(), 2.0);
                }
            }
        }
        calculatePotential();

        return (1.0 / 8.0) * a;
    }

    public Double calculateIntegral() {
        Double currentIntegralValue = calculateIntegralAtIteration();
        Double diff = 0.0;

        int k = 1;
        if (this.iterationIntegralContainer.getMyPairs().size() != 0)
            k = this.iterationIntegralContainer.getMyPairs().get(this.iterationIntegralContainer.getMyPairs().size() - 1).getIteration();
        this.iterationIntegralContainer.add(k++, currentIntegralValue);

        do {
            Double newIntegralValue = calculateIntegralAtIteration();
            this.iterationIntegralContainer.add(k++, newIntegralValue);

            diff = Math.abs(newIntegralValue - currentIntegralValue)/currentIntegralValue;
            currentIntegralValue = newIntegralValue;

        } while (diff > 10e-8);

        return currentIntegralValue;
    }

    private void evaluateInitialEdgesVonNuemann() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i < potentialPoints.length; i++) {
            for (int j = 0; j < potentialPoints[0].length; j++) {
                if (potentialPoints[i][j].getObstacle()) {
                    potentialPoints[i][j].setValue(0.0);
                } else {
                    potentialPoints[i][j].setValue(1.0 * potentialPoints[i][j].getX());
                }
            }
        }

        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);

    }

    public void vonNuemannInitialization() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int y = 0; y < potentialPoints.length; y++) {
            if (y < ((int) (matrixSpace.getBox().getyRange().getY() - this.obstaclePoints.getMyPairs().get(0).getY()) / matrixSpace.getJump()) &&
                    y >= 0) {

                int temp = (int) (this.obstaclePoints.getMyPairs().get(0).getX() / matrixSpace.getJump())-1;

                potentialPoints[y][temp].setValue(
                        potentialPoints[y][temp-1].getValue()
                );
            }

            if (y < ((int) (matrixSpace.getBox().getyRange().getY() -this.obstaclePoints.getMyPairs().get(2).getY()) / matrixSpace.getJump()) &&
                    y > ((int) (matrixSpace.getBox().getyRange().getY() -this.obstaclePoints.getMyPairs().get(1).getY()) / matrixSpace.getJump())) {

                int temp = ((int) (this.obstaclePoints.getMyPairs().get(2).getX() / matrixSpace.getJump()))-1;

                potentialPoints[y][temp].setValue(
                        potentialPoints[y][temp-1].getValue()
                );
            }

            if (y < ((int) (matrixSpace.getBox().getyRange().getY() - this.obstaclePoints.getMyPairs().get(3).getY()) / matrixSpace.getJump()) &&
                    y >= 0) {

                int temp = (int) ((this.obstaclePoints.getMyPairs().get(3).getX()) / matrixSpace.getJump())-1;

                potentialPoints[y][temp].setValue(
                        potentialPoints[y][temp+1].getValue()
                );
            }
        }

        for (int x = (int) (this.obstaclePoints.getMyPairs().get(0).getX() / matrixSpace.getJump());
             x < ((int) (this.obstaclePoints.getMyPairs().get(1).getX()) / matrixSpace.getJump())-1;
             x++) {

            int temp = (int) (potentialPoints.length - this.obstaclePoints.getMyPairs().get(0).getY() / matrixSpace.getJump());

            potentialPoints[temp][x].setValue(
                    potentialPoints[temp+1][x].getValue()
            );
        }

        for (int x = (int) (this.obstaclePoints.getMyPairs().get(2).getX() / matrixSpace.getJump());
             x < ((int) (this.obstaclePoints.getMyPairs().get(3).getX()) / matrixSpace.getJump())-1;
             x++) {

            int temp = (int) (potentialPoints.length - this.obstaclePoints.getMyPairs().get(2).getY() / matrixSpace.getJump());

            potentialPoints[temp][x].setValue(
                    potentialPoints[temp+1][x].getValue()
            );
        }

        int iA = (int) (this.obstaclePoints.getMyPairs().get(0).getX() / matrixSpace.getJump())-1;
        int jA = potentialPoints.length - (int) (this.obstaclePoints.getMyPairs().get(0).getY() / matrixSpace.getJump());

        potentialPoints[jA][iA].setValue(
                (potentialPoints[jA][iA-1].getValue() + potentialPoints[jA+1][iA].getValue())/2.0
        );

        int iC = (int) (this.obstaclePoints.getMyPairs().get(2).getX() / matrixSpace.getJump())-1;
        int jC = potentialPoints.length - (int) (this.obstaclePoints.getMyPairs().get(2).getY() / matrixSpace.getJump());

        potentialPoints[jC][iC].setValue(
                (potentialPoints[jC][iC-1].getValue() + potentialPoints[jC+1][iC].getValue())/2.0
        );

        int iD = (int) (this.obstaclePoints.getMyPairs().get(3).getX() / matrixSpace.getJump())-1;
        int jD = potentialPoints.length - (int) (this.obstaclePoints.getMyPairs().get(3).getY() / matrixSpace.getJump());

        potentialPoints[jD][iD].setValue(
                (potentialPoints[jD][iD+1].getValue() + potentialPoints[jD+1][iD].getValue())/2.0
        );

        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);
    }

    public void evaluateEdgePotential() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i < potentialPoints[0].length; i++) {
            if ((i >= 0 && i <= ((int) (this.obstaclePoints.getMyPairs().get(0).getX()) / matrixSpace.getJump() - 2)) ||
                    (i >= (int) this.obstaclePoints.getMyPairs().get(3).getX() / matrixSpace.getJump() ) && i <= potentialPoints[0].length - 1) {
                potentialPoints[0][i].setValue(
                        potentialPoints[1][i].getValue()
                );
            }
            potentialPoints[potentialPoints.length - 1][i].setValue(
                    potentialPoints[potentialPoints.length - 2][i].getValue()
            );
        }
        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);

        vonNuemannInitialization();
    }

    public MatrixSpace getMatrixSpace() {
        return matrixSpace;
    }

    public IterationIntegralContainer getIterationIntegralContainer() {
        return iterationIntegralContainer;
    }
}

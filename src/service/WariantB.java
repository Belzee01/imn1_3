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
        System.out.println("Creating wariant B");
        this.matrixSpace = matrixSpace;
        this.iterationIntegralContainer = new IterationIntegralContainer();

        this.omega = omega;
        evaluateInitialEdgeValues();
    }

    public WariantB(MatrixSpace matrixSpace, Obstacle obstaclePoints, Double omega) {
        this.matrixSpace = matrixSpace;
        this.obstaclePoints = obstaclePoints;
        this.iterationIntegralContainer = new IterationIntegralContainer();

        this.omega = omega;
        evaluateInitialEdgeValues();
    }

    public void evaluateInitialEdgeValues() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i <=  50.0/matrixSpace.getJump(); i++) { //lewy a
            int y = matrixSpace.getRows() - i -1;
            potentialPoints[y][0].setValue(
                    1.0 * (i - 0.0)
            );
        }

        for (int i = (int) (150.0/matrixSpace.getJump()); i <=  200.0/matrixSpace.getJump(); i++) { //prawy b
            int y = matrixSpace.getRows() - i-1;
            potentialPoints[y][matrixSpace.getColumns()-1].setValue(
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
            int y = (int) (matrixSpace.getRows() - 50.0/matrixSpace.getJump());
            potentialPoints[y-1][i].setValue(
                    potentialPoints[y-1][0].getValue()
            );
        }
        for (int i = (int) (50.0/matrixSpace.getJump()); i <=  200.0/matrixSpace.getJump(); i++) { //dolny
            int y = (int) (matrixSpace.getRows() - 50.0/matrixSpace.getJump());
            potentialPoints[matrixSpace.getRows()-i-1][(int) (100.0/matrixSpace.getJump())].setValue(
                    potentialPoints[y-1][0].getValue()
            );
        }
        for (int i = (int) (100.0/matrixSpace.getJump()); i <=  400.0/matrixSpace.getJump(); i++) { //dolny
            int y = (int) (matrixSpace.getRows() - 50.0/matrixSpace.getJump());
            potentialPoints[0][i].setValue(
                    potentialPoints[y-1][0].getValue()
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
        overRelaxation();

        return (1.0 / 8.0) * a;
    }

    public Double calculateIntegral() {
        System.out.println("Calculating integral");
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

        } while (diff > 10e-10);
        System.out.println("Finished calculating integral");

        return currentIntegralValue;
    }

    public MatrixSpace getMatrixSpace() {
        return matrixSpace;
    }

    public IterationIntegralContainer getIterationIntegralContainer() {
        return iterationIntegralContainer;
    }

    public Obstacle getObstaclePoints() {
        return obstaclePoints;
    }

    public TYPE getType() {
        return type;
    }

    public Double getOmega() {
        return omega;
    }
}

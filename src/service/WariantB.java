package service;

import helpers.IterationIntegralContainer;
import helpers.Obstacle;
import helpers.PotentialPoint;

public class WariantB {
    public enum TYPE {
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

    public WariantB(MatrixSpace matrixSpace, Double omega, TYPE type) {
        this.matrixSpace = matrixSpace;
        this.obstaclePoints = obstaclePoints;
        this.iterationIntegralContainer = new IterationIntegralContainer();
        this.type = TYPE.NEUMANN;

        this.omega = omega;
        evaluateEdgeZad2();
    }

    public void evaluateInitialEdgeValues() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i <= 50; i++) { //lewy a
            int y = matrixSpace.getRows() - i - 1;
            potentialPoints[y][0].setValue(
                    1.0 * (i * matrixSpace.getJump() - 0.0)
            );
        }

        for (int i = 150; i <= 200; i++) { //prawy b
            int y = matrixSpace.getRows() - i - 1;
            potentialPoints[y][400].setValue(
                    1.0 * (i * matrixSpace.getJump() - 1.5)
            );
        }

        //dolny
        for (int i = 0; i <= 300; i++) { //dolny
            potentialPoints[200][i].setValue(
                    potentialPoints[0][0].getValue()
            );
        }
        for (int i = 50; i <= 200; i++) { //dolny
            potentialPoints[i][300].setValue(
                    potentialPoints[0][0].getValue()
            );
        }
        for (int i = 300; i <= 400; i++) { //dolny
            potentialPoints[50][i].setValue(
                    potentialPoints[0][0].getValue()
            );
        }

        //gorny
        for (int i = 0; i <= 100; i++) { //dolny
            int y = 150;
            potentialPoints[y][i].setValue(
                    potentialPoints[150][0].getValue()
            );
        }
        for (int i = 0; i <= 150; i++) { //dolny
            potentialPoints[i][100].setValue(
                    potentialPoints[150][0].getValue()
            );
        }
        for (int i = 100; i <= 400; i++) { //dolny
            potentialPoints[0][i].setValue(
                    potentialPoints[150][0].getValue()
            );
        }
        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);
    }

    public void overRelaxation() {
        if (type == TYPE.NEUMANN) {
            evaluateEdgeNeumann();
        }

        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 199; i > 0; i--) {
            for (int j = 1; j < potentialPoints[0].length - 1; j++) {
                if (!matrixSpace.getDoubleMatrix().getMatrix()[i][j].getObstacle()) {
                    Double value = (1.0 - omega) * potentialPoints[i][j].getValue() + omega * (potentialPoints[i - 1][j].getValue() + potentialPoints[i][j - 1].getValue() +
                            potentialPoints[i + 1][j].getValue() + potentialPoints[i][j + 1].getValue()) / 4.0;
                    potentialPoints[i][j].setValue(value);
                }
            }
        }
        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);
    }

    private Double calculateIntegralAtIteration() {

        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        Double a = 0.0;

        for (int i = potentialPoints.length - 2; i > 0 ; i --) {
            for (int j = 1; j < potentialPoints[0].length - 1; j++) {
                if (!matrixSpace.getDoubleMatrix().getMatrix()[i][j].getObstacle()) {
                    a += (1.0 / 8.0) * (Math.pow(potentialPoints[i][j + 1].getValue() - potentialPoints[i][j - 1].getValue(), 2.0) +
                            Math.pow(potentialPoints[i - 1][j].getValue() - potentialPoints[i + 1][j].getValue(), 2.0));
                }
            }
        }
        return a;
    }

    public Double calculateIntegral() {
        overRelaxation();
        System.out.println("Calculating integral");
        Double currentIntegralValue = calculateIntegralAtIteration();
        Double diff = 0.0;

        int k = 1;
        if (this.iterationIntegralContainer.getMyPairs().size() != 0)
            k = this.iterationIntegralContainer.getMyPairs().get(this.iterationIntegralContainer.getMyPairs().size() - 1).getIteration();
        this.iterationIntegralContainer.add(k++, currentIntegralValue);

        do {
            overRelaxation();
            Double newIntegralValue = calculateIntegralAtIteration();
            this.iterationIntegralContainer.add(k++, newIntegralValue);

            diff = Math.abs(newIntegralValue - currentIntegralValue) / currentIntegralValue;
            currentIntegralValue = newIntegralValue;
            System.out.println("Calculating integral diff : " + diff);
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

    public void evaluateEdgeZad2() {
        evaluateEdgeDirchlet();
        evaluateEdgeNeumann();
    }

    public void evaluateEdgeDirchlet() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 0; i <= 0.5 / matrixSpace.getJump(); i++) { //lewy a
            int y = matrixSpace.getRows() - i - 1;
            potentialPoints[y][0].setValue(
                    1.0 * 0.0
            );
        }

        for (int i = (int) (1.5 / matrixSpace.getJump()); i <= 2.0 / matrixSpace.getJump(); i++) { //prawy b
            int y = matrixSpace.getRows() - i - 1;
            potentialPoints[y][matrixSpace.getColumns() - 1].setValue(
                    1.0 * (matrixSpace.getJump() * 400)
            );
        }
        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);
    }

    public void evaluateEdgeNeumann() {
        PotentialPoint[][] potentialPoints = matrixSpace.getDoubleMatrix().getMatrix();

        for (int i = 1; i <= 99; i++) { //lewy a
            int y = 150;
            potentialPoints[y][i].setValue(
                    potentialPoints[y + 1][i].getValue()
            );
        }

        for (int i = 1; i < 150; i++) { //lewy a
            int x = 100;
            potentialPoints[i][x].setValue(
                    potentialPoints[i][x + 1].getValue()
            );
        }

        for (int i = 101; i <= 399; i++) { //lewy a
            int y = 0;
            potentialPoints[y][i].setValue(
                    potentialPoints[y + 1][i].getValue()
            );
        }

        for (int i = 1; i <= 299; i++) { //lewy a
            int y = 200;
            potentialPoints[y][i].setValue(
                    potentialPoints[y - 1][i].getValue()
            );
        }

        for (int i = 199; i > 50; i--) { //lewy a
            int x = 300;
            potentialPoints[i][x].setValue(
                    potentialPoints[i][x - 1].getValue()
            );
        }

        for (int i = 301; i <= 399; i++) { //lewy a
            int y = 50;
            potentialPoints[y][i].setValue(
                    potentialPoints[y - 1][i].getValue()
            );
        }

        potentialPoints[150][100].setValue((potentialPoints[151][100].getValue() + potentialPoints[150][101].getValue()) / 2.0);
        potentialPoints[50][300].setValue((potentialPoints[49][300].getValue() + potentialPoints[50][299].getValue()) / 2.0);

        matrixSpace.getDoubleMatrix().setMatrix(potentialPoints);
    }
}

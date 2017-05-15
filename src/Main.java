import fileproc.AdvancedOutputFile;
import fileproc.CustomFileWriter;
import helpers.*;
import service.MatrixSpace;
import service.WariantA;
import service.WariantB;

import java.util.Arrays;

public class Main {

    public static void zestawA() {
        final BoundingBox boundingBox = new BoundingBox(new MyPair(1.0, 200.0), new MyPair(1.0, 100.0));

        MatrixSpace matrixSpace = new MatrixSpace(
                //                                  rows          columns
                MatrixBuilder.buildIntegerMatrix(100, 200),
                MatrixBuilder.buildDoubleMatrix(100, 200),
                boundingBox,
                1.0
        );

        final Obstacle obstacle = new Obstacle()
                .addNewObstaclePoint(85.0, 100.0)
                .addNewObstaclePoint(85.0, 85.0) //A
                .addNewObstaclePoint(100.0, 85.0) //B
                .addNewObstaclePoint(100.0, 70.0) //C
                .addNewObstaclePoint(115.0, 70.0) //D
                .addNewObstaclePoint(115.0, 100.0)
                .addNewObstaclePoint(85.0, 100.0);

        matrixSpace.addObstacle(obstacle);

        WariantA wariantA = new WariantA(matrixSpace);

        System.out.println(wariantA.calculateIntegral());

        PotentialPoint[][] temp = wariantA.getMatrixSpace().getDoubleMatrix().getMatrix();

        for (PotentialPoint[] p : temp) {
            System.out.println(Arrays.toString(p));
        }

        CustomFileWriter.writeToFile(
                new AdvancedOutputFile(temp, boundingBox, 1.0, "warA_pot.dat")
        );

        CustomFileWriter.writeToFile(wariantA.getIterationIntegralContainer(), "warA_integral.dat");


        ///Zadanie 2
        matrixSpace = new MatrixSpace(
                //                                  rows          columns
                MatrixBuilder.buildIntegerMatrix(100, 200),
                MatrixBuilder.buildDoubleMatrix(100, 200),
                boundingBox,
                1.0
        );

        matrixSpace.addObstacle(obstacle);

        Obstacle obstaclePoints = new Obstacle()
                .addNewObstaclePoint(85.0, 85.0) //A
                .addNewObstaclePoint(100.0, 85.0) //B
                .addNewObstaclePoint(100.0, 70.0) //C
                .addNewObstaclePoint(115.0, 70.0); //D

        WariantA wariantA1 = new WariantA(matrixSpace, obstaclePoints);

        System.out.println(wariantA1.calculateIntegral());

        temp = wariantA1.getMatrixSpace().getDoubleMatrix().getMatrix();

        for (PotentialPoint[] p : temp) {
            for (int i = 0; i < p.length; i++) {
                System.out.print(p[i] + "\t\t");
            }
            System.out.println();
        }

        CustomFileWriter.writeToFile(
                new AdvancedOutputFile(temp, boundingBox, 1.0, "warA_pot2.dat")
        );

        CustomFileWriter.writeToFile(wariantA1.getIterationIntegralContainer(), "warA_integral2.dat");
    }

    public static void zestawB() {
        final BoundingBox boundingBox = new BoundingBox(new MyPair(0.0, 400.0), new MyPair(0.0, 200.0));

        final Double jump = 1.0;

        MatrixSpace matrixSpace = new MatrixSpace(
                //                                  rows          columns
                MatrixBuilder.buildIntegerMatrix((int) (201/jump), (int) (401/jump)),
                MatrixBuilder.buildDoubleMatrix((int) (201/jump), (int) (401/jump)),
                boundingBox,
                jump
        );

        final Obstacle obstacle1 = new Obstacle()
                .addNewObstaclePoint(0.0, 50.0)
                .addNewObstaclePoint(100.0, 50.0)
                .addNewObstaclePoint(100.0, 200.0)
                .addNewObstaclePoint(0.0, 200.0)
                .addNewObstaclePoint(0.0, 50.0);

        final Obstacle obstacle2 = new Obstacle()
                .addNewObstaclePoint(300.0, 0.0)
                .addNewObstaclePoint(400.0, 0.0)
                .addNewObstaclePoint(400.0, 150.0)
                .addNewObstaclePoint(300.0, 150.0)
                .addNewObstaclePoint(300.0, 0.0);

        matrixSpace.addObstacle(obstacle1).addObstacle(obstacle2);

        WariantB wariantB = new WariantB(matrixSpace, 1.99);

        wariantB.calculateIntegral();

        PotentialPoint[][] potentialPoints = wariantB.getMatrixSpace().getDoubleMatrix().getMatrix();

        for (int i = 0; i < potentialPoints.length; i++) {
            for (int j = 0; j < potentialPoints[0].length; j++) {
                System.out.print(potentialPoints[i][j].getValue() + "\t\t");
            }
            System.out.println();
        }

        PotentialPoint[][] temp = wariantB.getMatrixSpace().getDoubleMatrix().getMatrix();

        CustomFileWriter.writeToFile(
                new AdvancedOutputFile(temp, boundingBox, jump, "warB_pot.dat")
        );

        CustomFileWriter.writeToFile(wariantB.getIterationIntegralContainer(), "warB_integral.dat");
    }

    public static void main(String[] args) {


        zestawB();

        System.out.println("Hello World!");
    }
}

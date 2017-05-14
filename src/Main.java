import fileproc.AdvancedOutputFile;
import fileproc.CustomFileWriter;
import helpers.*;
import service.MatrixSpace;
import service.WariantA;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

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


        System.out.println("Hello World!");
    }
}

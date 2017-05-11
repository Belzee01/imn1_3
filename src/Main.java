import helpers.BoundingBox;
import helpers.MatrixBuilder;
import helpers.MyPair;
import helpers.Obstacle;
import service.MatrixSpace;
import service.WariantA;

public class Main {

    public static void main(String[] args) {

        MatrixSpace matrixSpace = new MatrixSpace(
                //                                  rows          columns
                MatrixBuilder.buildIntegerMatrix(100, 200),
                MatrixBuilder.buildDoubleMatrix(100, 200),
                new BoundingBox(new MyPair(1.0, 200.0), new MyPair(1.0, 100.0)),
                1.0
        );

        matrixSpace.addObstacle(
                new Obstacle()
                        .addNewObstaclePoint(85.0, 100.0)
                        .addNewObstaclePoint(85.0, 85.0)
                        .addNewObstaclePoint(100.0, 85.0)
                        .addNewObstaclePoint(100.0, 70.0)
                        .addNewObstaclePoint(115.0, 70.0)
                        .addNewObstaclePoint(115.0, 100.0)
                        .addNewObstaclePoint(85.0, 100.0)
        );

        WariantA wariantA = new WariantA(matrixSpace);

        System.out.println("Hello World!");
    }
}

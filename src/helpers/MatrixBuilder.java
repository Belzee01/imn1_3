package helpers;

public class MatrixBuilder {

    public static IntegerMatrix buildIntegerMatrix(int rows, int columns) {
        return new IntegerMatrix(rows, columns);
    }

    public static PotentialMatrix buildDoubleMatrix(int rows, int columns) {
        return new PotentialMatrix(rows, columns);
    }

    public static class IntegerMatrix {
        private Integer[][] matrix;

        public IntegerMatrix(int rows, int columns) {
            this.matrix = new Integer[rows][columns];
            initialize();
        }

        private void initialize() {
            for (Integer[] integers:matrix) {
                for (Integer integer:integers) {
                    integer = 0;
                }
            }
        }

        public void setValue(int x, int y, Integer value) {
            this.matrix[x][y] = value;
        }

        public Integer[][] getMatrix() {
            return matrix;
        }
    }

    public static class PotentialMatrix {
        private PotentialPoint[][] matrix;

        public PotentialMatrix(int rows, int columns) {
            this.matrix = new PotentialPoint[rows][columns];
            initialize();
        }

        private void initialize() {
            for (PotentialPoint[] doubles:matrix) {
                for (PotentialPoint aDouble:doubles) {
                    aDouble = new PotentialPoint();
                }
            }
        }

        public void setPotentialPointIsObstacle(int x, int y, Boolean isObstacle) {
            this.matrix[x][y].setObstacle(isObstacle);
        }

        public PotentialPoint[][] getMatrix() {
            return matrix;
        }
    }
}

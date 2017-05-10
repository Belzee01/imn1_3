package helpers;

public class MatrixBuilder {

    public static IntegerMatrix buildIntegerMatrix(int rows, int columns) {
        return new IntegerMatrix(rows, columns);
    }

    public static DoubleMatrix buildDoubleMatrix(int rows, int columns) {
        return new DoubleMatrix(rows, columns);
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

    public static class DoubleMatrix {
        private Double[][] matrix;

        public DoubleMatrix(int rows, int columns) {
            this.matrix = new Double[rows][columns];
            initialize();
        }

        private void initialize() {
            for (Double[] doubles:matrix) {
                for (Double aDouble:doubles) {
                    aDouble = 0.0;
                }
            }
        }

        public Double[][] getMatrix() {
            return matrix;
        }
    }
}

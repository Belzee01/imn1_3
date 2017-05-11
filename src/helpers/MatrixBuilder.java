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
        private int rows;
        private int columns;

        public IntegerMatrix(int rows, int columns) {
            this.matrix = new Integer[rows][columns];
            this.rows = rows;
            this.columns = columns;

            initialize();
        }

        private void initialize() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    this.matrix[i][j] = 0;
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
        private int rows;
        private int columns;

        public PotentialMatrix(int rows, int columns) {
            this.matrix = new PotentialPoint[rows][columns];
            this.rows = rows;
            this.columns = columns;

            initialize();
        }

        //TODO setting x and y by jump parameter
        private void initialize() {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    this.matrix[i][j] = new PotentialPoint();
                }
            }
        }

        public void setPotentialPointIsObstacle(int x, int y, Boolean isObstacle) {
            this.matrix[x][y].setObstacle(isObstacle);
        }

        public void setMatrix(PotentialPoint[][] matrix) {
            this.matrix = matrix;
        }

        public PotentialPoint[][] getMatrix() {
            return matrix;
        }
    }
}

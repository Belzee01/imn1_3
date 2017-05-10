package helpers;

public class Matrix {

    private double[][] matrix;

    public Matrix(int rows, int columns) {
        this.matrix = new double[rows][columns];
    }

    public double[][] getMatrix() {
        return matrix;
    }
}

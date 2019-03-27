package tasks;

import static java.lang.Math.abs;

public class FindPivotRow extends Thread {

    private double[][] matrix;
    private int matrixSize;
    private int currentColumn;
    private int result;

    public FindPivotRow(double[][] matrix, int matrixSize, int currentColumn) {
        this.matrix = matrix;
        this.matrixSize = matrixSize;
        this.currentColumn = currentColumn;
    }

    public int getResult() {
        return result;
    }

    private boolean isZero(double x) {
        return abs(x) < 0.000001;
    }

    private int findNewPivotRow() {
        for (int column = currentColumn; column < matrixSize; column++) {
            if (!isZero(matrix[column][currentColumn]))
                return column;
        }
        return -1;
    }

    @Override
    public void run() {
        result = findNewPivotRow();
    }
}

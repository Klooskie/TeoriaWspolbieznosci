package tasks;

public class DividePivotCell extends Thread {

    private double[][] matrix;
    private int pivotRow;
    private int column;
    private double pivotRowFactor;

    public DividePivotCell(double[][] matrix, int pivotRow, int column, double pivotRowFactor) {
        this.matrix = matrix;
        this.pivotRow = pivotRow;
        this.column = column;
        this.pivotRowFactor = pivotRowFactor;
    }

    @Override
    public void run() {
        matrix[pivotRow][column] /= pivotRowFactor;
    }
}

package tasks;

public class SubtractPivotCellFromNormalCell extends Thread {

    private double[][] matrix;
    private int pivotRow;
    private int row;
    private int column;
    private double factor;

    public SubtractPivotCellFromNormalCell(double[][] matrix, int pivotRow, int row, int column, double factor) {
        this.matrix = matrix;
        this.pivotRow = pivotRow;
        this.row = row;
        this.column = column;
        this.factor = factor;
    }

    @Override
    public void run() {
        matrix[row][column] -= matrix[pivotRow][column] * factor;
    }
}

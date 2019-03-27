package tasks;

public class SwapPivotRowCell extends Thread {

    private double[][] matrix;
    private int currentRow;
    private int pivotRow;
    private int column;

    public SwapPivotRowCell(double[][] matrix, int currentRow, int pivotRow, int column) {
        this.matrix = matrix;
        this.currentRow = currentRow;
        this.pivotRow = pivotRow;
        this.column = column;
    }

    @Override
    public void run() {
        double tmp = matrix[currentRow][column];
        matrix[currentRow][column] = matrix[pivotRow][column];
        matrix[pivotRow][column] = tmp;
    }
}

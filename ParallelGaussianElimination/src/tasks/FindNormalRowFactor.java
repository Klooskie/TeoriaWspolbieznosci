package tasks;

public class FindNormalRowFactor extends Thread {

    private double[][] matrix;
    private int pivotRow;
    private int row;
    private double result;

    public FindNormalRowFactor(double[][] matrix, int pivotRow, int row) {
        this.matrix = matrix;
        this.pivotRow = pivotRow;
        this.row = row;
    }

    public double getResult() {
        return result;
    }

    @Override
    public void run() {
        if (row == pivotRow)
            result = 0;
        else
            result = matrix[row][pivotRow] / matrix[pivotRow][pivotRow];
    }
}

package tasks;

public class FindPivotRowFactor extends Thread {

    private double[][] matrix;
    private int currentColumn;
    private double result;

    public FindPivotRowFactor(double[][] matrix, int currentColumn) {
        this.matrix = matrix;
        this.currentColumn = currentColumn;
    }

    public double getResult() {
        return result;
    }

    @Override
    public void run() {
        result = matrix[currentColumn][currentColumn];
    }
}

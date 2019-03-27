import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tasks.*;

public class Main {

    public static void gaussianElimination(int matrixSize, double[][] matrix) throws InterruptedException {

        for (int i = 0; i < matrixSize; i++) {

            // Pierwsza klasa Foaty - znalezienie wiersza pivota
            FindPivotRow findPivotRow = new FindPivotRow(matrix, matrixSize, i);
            findPivotRow.start();
            findPivotRow.join();
            int pivotRow = findPivotRow.getResult();
            if(pivotRow == -1){
                System.out.println("Bad matrix");
                System.exit(1);
            }

            // Druga klasa Foaty - ustawienie pivota w odpowiednim wierszu
            List<SwapPivotRowCell> swapPivotRowCells = new ArrayList<>();
            for (int column = 0; column <= matrixSize; column++)
                swapPivotRowCells.add(new SwapPivotRowCell(matrix, i, pivotRow, column));
            for (SwapPivotRowCell swapPivotRowCell : swapPivotRowCells)
                swapPivotRowCell.start();
            for (SwapPivotRowCell swapPivotRowCell : swapPivotRowCells)
                swapPivotRowCell.join();

            // Trzecia klasa Foaty - obliczenie wspolczynnika przez ktory nalezy podzielic pivot
            FindPivotRowFactor findPivotRowFactor = new FindPivotRowFactor(matrix, i);
            findPivotRowFactor.start();
            findPivotRowFactor.join();
            double pivotRowFactor = findPivotRowFactor.getResult();

            // Czwarta klasa Foaty - podzielenie komorek pivota
            List<DividePivotCell> dividePivotCells = new ArrayList<>();
            for (int column = 0; column <= matrixSize; column++)
                dividePivotCells.add(new DividePivotCell(matrix, i, column, pivotRowFactor));
            for (DividePivotCell dividePivotCell : dividePivotCells)
                dividePivotCell.start();
            for (DividePivotCell dividePivotCell : dividePivotCells)
                dividePivotCell.join();


            // Piata klasa Foaty - znalezienie wspolczynnika dla kazdego wiersza przez ktory nalezy przemnozyc pivot go
            List<FindNormalRowFactor> findNormalRowFactors = new ArrayList<>();
            for (int row = 0; row < matrixSize; row++) {
                findNormalRowFactors.add(new FindNormalRowFactor(matrix, i, row));
            }
            for (FindNormalRowFactor findNormalRowFactor : findNormalRowFactors)
                findNormalRowFactor.start();
            for (FindNormalRowFactor findNormalRowFactor : findNormalRowFactors)
                findNormalRowFactor.join();
            double[] rowFactors = new double[matrixSize];
            for (int row = 0; row < matrixSize; row++)
                rowFactors[row] = findNormalRowFactors.get(row).getResult();


            // Szósta klasa Foaty - odjecie odpowiednio przemnozonych komorek pivota
            List<SubtractPivotCellFromNormalCell> subtractCells = new ArrayList<>();
            for (int row = 0; row < matrixSize; row++)
                for (int column = 0; column <= matrixSize; column++)
                    subtractCells.add(new SubtractPivotCellFromNormalCell(matrix, i, row, column, rowFactors[row]));
            for (SubtractPivotCellFromNormalCell subtractCell : subtractCells)
                subtractCell.start();
            for (SubtractPivotCellFromNormalCell subtractCell : subtractCells)
                subtractCell.join();

        }
    }


    private static void printResultMatrix(double[][] matrix, int matrixSize) {
        System.out.println(matrixSize);
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++)
                System.out.print(matrix[i][j] + " ");
            System.out.println();
        }
        for (int i = 0; i < matrixSize; i++)
            System.out.print(matrix[i][matrixSize] + " ");
    }


    public static void main(String[] args) {

        // wczytanie danych wejsciowych
        Scanner scanner = new Scanner(System.in);
        int matrixSize = scanner.nextInt();
        double[][] matrix = new double[matrixSize][matrixSize + 1];
        for (int row = 0; row < matrixSize; row++)
            for (int column = 0; column < matrixSize; column++)
                matrix[row][column] = scanner.nextDouble();
        for (int row = 0; row < matrixSize; row++)
            matrix[row][matrixSize] = scanner.nextDouble();

        // eliminacja gaussa
        try {
            gaussianElimination(matrixSize, matrix);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //wypisanie wynikow
        printResultMatrix(matrix, matrixSize);

    }

}

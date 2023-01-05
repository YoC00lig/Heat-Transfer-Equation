package org.solver;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Solver {
    private final int pointsNumber;
    private int denominator;
    private static final int domain =2;

    public Solver(int pointsNumber) {
        this.pointsNumber = pointsNumber;
        this.denominator = Math.abs(domain / pointsNumber);
    }

    private static double K(double x) {
        return (0 <= x && x <= 1) ? 1.0 : 2.0;
    }

    private void solve_equation(int size){
        RealMatrix matrix = new Array2DRowRealMatrix(size, size);

        for (int row = 0; row < size; row++){ // initializing matrix
            for (int col = 0; col < size; col++) matrix.setEntry(row, col, 0);
        }

    }

    private double[] get_from_to(int i, double value){
        if (i > 0 && i < this.pointsNumber) return new double[]{value - denominator, value + denominator};
        else if (i == 0) return  new double[]{0, value + denominator};
        else return new double[]{domain - denominator, domain};
    }

    private void BUV(int i, int j){

    }
}
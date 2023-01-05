package org.solver;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;

public class Solver {
    private final int pointsNumber;
    private int denominator;
    private static final int domain = 2;
    private final double h;
    IterativeLegendreGaussIntegrator gauss_integrator;

    public Solver(int pointsNumber) {
        this.pointsNumber = pointsNumber;
        this.denominator = Math.abs(domain / pointsNumber);
        this.h = 2.0 / pointsNumber;
    }

    private static double K(double x) {
        return (0 <= x && x <= 1) ? 1.0 : 2.0;
    }

    private void solve_equation(int size){
        RealMatrix matrix = new Array2DRowRealMatrix(size, size);

        for (int row = 0; row < size; row++){ // initializing matrix
            for (int col = 0; col < size; col++) matrix.setEntry(row, col, 0);
        }

        for (int row = 0; row < size; row++){ // initializing matrix
            for (int col = 0; col < row + 1; col++) {
                if (col < size && Math.abs(row-col) <= 1) matrix.setEntry(row, col, BUV(row, col));
            }
        }
    }

    // funkcja ek(x) i pochodna funkcji ek(x)
    private double e(double x, int k){
        if (h * (k-1) <= x && x <= h*k) return (x - h*k + h)/h;
        else if (h * k <= x && x <= h*(k+1)) return (h*k + h - x)/h;
        else return 0;
    }

    private double e_derative(double x, int k){
        if (h * (k-1) <= x && x <= h*k) return 1/h;
        else if (h * k <= x && x <= h*(k+1)) return -1/h;
        else return 0;
    }

    // wyznaczanie dziedziny pochodnej
    private double[] get_from_to(int i, double value){
        if (i > 0 && i < this.pointsNumber) return new double[]{value - denominator, value + denominator};
        else if (i == 0) return  new double[]{0, value + denominator};
        else return new double[]{domain - denominator, domain};
    }

    private double[] get_integrate_range(int i, int j){
        double from = Math.round(Math.max(get_from_to(i, i*denominator)[0], get_from_to(j, j*denominator)[0]));
        double to = Math.round(Math.max(get_from_to(i, i*denominator)[1], get_from_to(j, j*denominator)[1]));
        return new double[]{from, to};
    }

    // funkcja B(u,v)
    private double BUV(int i, int j){
        double[] range =  get_integrate_range(i, j);
        double from = range[0];
        double to = range[1];

        double first_integrate = gauss_integrator.integrate(
                Integer.MAX_VALUE, x -> K(x) * e_derative(x, i) * e_derative(x, j), from, to
        );

        double second_value_to_subtract = (i < 2 && j < 2) ? -K(0)*e(0,i)*e(0,j) : 0;

        return first_integrate - second_value_to_subtract;
    }

    // funkcja L(v)
    private double LV(int i) {return  -K(0)*20*e(0,i);}
}
package org.solver;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.linear.LUDecomposition;

public class Solution {
    private final int n;
    private final IterativeLegendreGaussIntegrator gauss_integrator;
    private final double domain = 2.0;
    private final double h;

    public Solution(int n) {
        this.gauss_integrator= new IterativeLegendreGaussIntegrator(2, Math.pow(1,-5), Math.pow(1,-5));
        this.n = n;
        this.h = domain / n;
    }

    private static double K(double x) {
        return (0 <= x && x <= 1) ? 1.0 : 2.0;
    }

    private RealVector solve_equation(int size){
        RealMatrix B = new Array2DRowRealMatrix(size, size);
        for (int row = 0; row < size; row++){ // tworzenie macierzy B(u,v)
            for (int col = 0; col < row + 1; col++) {
                if (col < size && Math.abs(row-col) <= 1) {
                    System.out.println(row + " " + col + " " + BUV(row, col));
                    B.setEntry(row, col, BUV(row, col));
                }
            }
        }
        RealVector L = LV(size);
        return new LUDecomposition(B).getSolver().solve(L);
    }

    // funkcja ek(x) i pochodna funkcji ek(x)
    private double e(double x, int k){
        if (h * (k-1) <= x && x <= h*k) return (x - h*k + h)/h;
        else if (h * k <= x && x <= h*(k+1)) return (h*k + h - x)/h;
        else return 0;
    }

    private double e_derivative(double x, double k){
        if (h * (k-1) <= x && x <= h*k) return 1/h;
        else if (h * k < x && x <= h*(k+1)) return -1/h;
        else return 0;
    }

    // wyznaczanie dziedziny
    private double[] get_from_to(int i, double value){
        if (i > 0 && i < n) return new double[]{value - h, value + h};
        else if (i == 0) return  new double[]{0, h};
        else return new double[]{domain - h, domain};
    }

    private double[] get_integrate_range(int i, int j){
        double from = Math.max(get_from_to(i, i*h)[0], get_from_to(j, j*h)[0]);
        double to = Math.min(get_from_to(i, i*h)[1], get_from_to(j, j*h)[1]);
        return new double[]{from, to};
    }

    // funkcja B(u,v)
    private double BUV(int i, int j){
        double[] range =  get_integrate_range(i, j);
        double first_integrate = gauss_integrator.integrate(Integer.MAX_VALUE, x -> K(x) * e_derivative(x, i) * e_derivative(x, j), range[0], range[1]); // całka
        double second_value_to_subtract = (i < 2 && j < 2) ? -K(0)*e(0,i)*e(0,j) : 0; // -k(0)v(0)u(o)
        return first_integrate - second_value_to_subtract;
    }

    // macierz L(v)
    private RealVector LV(int size) {
        RealVector L = new ArrayRealVector(size, 0);
        for (int i = 0 ; i < size; i ++) L.setEntry(i, -K(0)*20*e(0,i));
        return L;
    }

    public double[] get_solution() {
        return this.solve_equation(n).toArray();
    }
}
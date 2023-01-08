package org.solver;

import org.apache.commons.math3.analysis.integration.IterativeLegendreGaussIntegrator;
import org.apache.commons.math3.linear.*;

public class Solution {

    private final double n;
    private final IterativeLegendreGaussIntegrator gauss_integrator;
    private final double domain = 2.0;
    private final double h;


    public Solution(int number_of_elements) {
        this.gauss_integrator= new IterativeLegendreGaussIntegrator(2, Math.pow(0.1,4), Math.pow(0.1,4));
        this.n = number_of_elements;
        this.h = domain / n;
    }

    // e functions
    private double e(double x, int k){
        if (Math.max(h * (k-1), 0) <= x && x <= h*k) return (x - h*(k-1))/h;
        else if (h * k < x && x <= Math.min(h*(k+1),2)) return (h*(k+1) - x)/h;
        else return 0;
    }

    private double ePrim(double x, double k){
        if (Math.max(h * (k-1),0) <= x && x <= h*k) return 1/h;
        else if (h * k < x && x <= Math.min(h*(k+1),2)) return -1/h;
        else return 0;
    }

    private RealVector solve_equation(double size) {
        RealMatrix B = new Array2DRowRealMatrix((int) size, (int) size);
        for (int row = 0; row < size; row++){ // tworzenie macierzy B(u,v)
            for (int col = 0; col <= row + 1; col++) {
                if (col < size && Math.abs(row-col) <= 1) B.setEntry(row, col, BUV(row, col));
            }
        }
        RealVector L = LV(size); // L(v)
        return new LUDecomposition(B).getSolver().solve(L);
    }

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
    // B(u,v) i L(v)
    private double BUV( int i, int j) {
        double[] range =  get_integrate_range(i, j);
        double integral = gauss_integrator.integrate(Integer.MAX_VALUE, x ->  ePrim(x, i) * ePrim(x, j), range[0], range[1]);
        double second_value_to_subtract = (i < 2 && j <= 2) ? e(0,i)*e(0,j) : 0;
        return integral - second_value_to_subtract;
    }

    private RealVector LV(double size) {
        RealVector L = new ArrayRealVector((int) size, 0);
        for (int i = 0 ; i < size; i ++) L.setEntry(i, -20*e(0,i));
        return L;
    }

    public double[] get_solution() {
        return this.solve_equation(n).toArray();
    }
}

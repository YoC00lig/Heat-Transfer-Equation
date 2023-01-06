package org.solver;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {
    private LineChart<Number, Number> chart;
    private XYChart.Series series = new XYChart.Series();
    private double[] result;
    private int n;

    public static void main(String[] args) {
        launch(args);
    }

    public void init() {
        LineChartsCreate();
        List<String> args = getParameters().getRaw();
        n = Integer.parseInt(args.get(0));
        Solution s = new Solution(n);
        result = s.get_solution();
    }

    @Override
    public void start(Stage primaryStage) {
        addToSeries();
        Scene scene = new Scene(chart, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void LineChartsCreate() {
        final NumberAxis x = new NumberAxis();
        final NumberAxis y = new NumberAxis();

        x.setLabel("x");
        y.setLabel("u");

        chart = new LineChart(x, y);
        chart.getData().add(series);
        chart.setTitle("Heat transfer equation");
        chart.setCreateSymbols(false);
    }

    public void addToSeries() {
        for(int i = 0; i< result.length;i++) {
            double scaled = 2.0 * i / (result.length - 1);
            series.getData().add(new XYChart.Data<>(scaled, result[i]));

        }
    }

}

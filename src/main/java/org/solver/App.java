package org.solver;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    }

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("INPUT NUMBER:");
        Button button = new Button("DRAW GRAPH");
        TextField input = new TextField("50");
        VBox box = new VBox();
        box.getChildren().addAll(label, input, button);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        input.setMaxWidth(100);
        input.setAlignment(Pos.CENTER);

        button.setOnAction(event -> {
            n = Integer.parseInt(input.getText());
            Solution s = new Solution(n);
            result = s.get_solution();
            addToSeries();
            Scene scene = new Scene(chart, 500, 500);
            primaryStage.setScene(scene);
            primaryStage.show();
        });

        Scene start_scene = new Scene(box, 500, 500);
        primaryStage.setScene(start_scene);
        primaryStage.show();
    }

    public void LineChartsCreate() {
        final NumberAxis x = new NumberAxis();
        final NumberAxis y = new NumberAxis();

        x.setLabel("x");
        y.setLabel("f(x)");

        chart = new LineChart(x, y);
        chart.getData().add(series);
        chart.setTitle("Heat transfer equation");
        chart.setCreateSymbols(false);
    }

    public void addToSeries() {
        for(int i = 0; i< result.length;i++) {
            double scaled = 2.0 * i / (result.length);
            series.getData().add(new XYChart.Data<>(scaled, result[i]));

        }
    }

}
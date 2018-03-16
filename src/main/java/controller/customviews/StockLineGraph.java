package main.java.controller.customviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;

public class StockLineGraph extends LineChart<String, Number> {
    private ObservableList<XYChart.Series<String, Number>> data;
    private NumberAxis yAxis;
    private CategoryAxis xAxis;

    public StockLineGraph(){
        this(new CategoryAxis(), new NumberAxis());
    }

    private StockLineGraph(CategoryAxis xAxis, NumberAxis yAxis) {
        super(xAxis, yAxis);
        this.yAxis = yAxis;
        this.xAxis = xAxis;


        formatChart();

        setData(FXCollections.observableArrayList());
    }

    private void formatChart() {
        setAnimated(false);
        verticalGridLinesVisibleProperty().set(false);

        formatXAxis();
        formatYAxis();
    }

    private void formatYAxis() {
        yAxis.autoRangingProperty().setValue(true);
        yAxis.forceZeroInRangeProperty().setValue(false);
        yAxis.setAnimated(false);


    }

    private void formatXAxis() {
        xAxis.setAnimated(false);
    }
}

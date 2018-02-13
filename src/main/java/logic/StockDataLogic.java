package main.java.logic;

import com.zoicapital.stockchartsfx.BarData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.util.StockData;
import main.java.util.StockTimeSeriesIntradayInterval;
import main.java.util.StockTimeSeriesType;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class StockDataLogic {

    // TODO add conversion methods for candlestick chart and normal line chart

    // TODO make chart data return a series of data that can be added straight to the chart
    public static ObservableList<XYChart.Series<String, Number>> getCandleStickChartData(){
        // values must be double and date must be gregorian calendar
        List<StockData> rawData = CurrentStockInformation.getInstance().getChartData();

        // convert the data to a chart ready format
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        SimpleDateFormat sdf = getXAxisFormat();
        for (StockData d : rawData) {
            // convert to bar data because it is required by the candlestick chart API
            BarData convertedData = toBarData(d);
            //
            String XAxislabel = sdf.format(convertedData.getDateTime().getTime());
            series.getData().add(new XYChart.Data<>(XAxislabel, convertedData.getOpen(), convertedData));
        }

        // can be added directly to the chart without further modifications
        return FXCollections.<XYChart.Series<String, Number>>observableArrayList(series);
    }


    /**
     * ensures no redundant data on X axis with proper formatting
     * @return
     */
    private static SimpleDateFormat getXAxisFormat() {

        StockTimeSeriesType function = CurrentStockInformation.getInstance().getFunction();
        StockTimeSeriesIntradayInterval interval = CurrentStockInformation.getInstance().getInterval();

        if(function == StockTimeSeriesType.TIME_SERIES_DAILY){
            // now check the interval

            if(interval == StockTimeSeriesIntradayInterval.T60){
                return new SimpleDateFormat("Ka / dd-MMM");
            }else { // else if interval is 1/5/15/30 minutes
                return new SimpleDateFormat("K:mma / dd-MMM");
            }

        }
        else if (function != StockTimeSeriesType.TIME_SERIES_MONTHLY){ // if function is daily or weekly

            return new SimpleDateFormat("dd-MMM-yy");
        }else{ // if function is monthly
            return new SimpleDateFormat("MM-yyyy");
        }

    }

    public static XYChart.Series getLineChartData(){
        ArrayList<StockData> rawData = CurrentStockInformation.getInstance().getChartData();

        return null;
    }

    private static BarData toBarData(StockData data){

        double open = data.getOpen().doubleValue();
        double high = data.getHigh().doubleValue();
        double low = data.getLow().doubleValue();
        double close = data.getClose().doubleValue();
        long volume = data.getVolume();

        // Stock Data stores the dateTime as an Instant and the candlestick chart api requires GregorianCalendar
        // conversion from Instant -> ZonedDateTime -> GregorianCalendar happens here
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(data.getDateTime(), ZoneId.systemDefault());
        GregorianCalendar dateTime = GregorianCalendar.from(zonedDateTime);


        BarData barData = new BarData(dateTime, open, high, low, close, volume);

        return barData;
    }
}

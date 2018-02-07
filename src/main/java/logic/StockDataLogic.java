package main.java.logic;

import com.zoicapital.stockchartsfx.BarData;
import javafx.scene.chart.XYChart;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.util.StockData;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class StockDataLogic {

    // TODO add conversion methods for candlestick chart and normal line chart

    public static List<BarData> getCandleStickChartData(){
        // values must be double and date must be gregorian calendar
        List<StockData> rawData = CurrentStockInformation.getInstance().getChartData();
        List<BarData> convertedData = new ArrayList<>();

        // convert each data point and add it to the converted list
        for (StockData d : rawData) {
            convertedData.add(toBarData(d));
            System.out.println(toBarData(d));
        }

        return convertedData;
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

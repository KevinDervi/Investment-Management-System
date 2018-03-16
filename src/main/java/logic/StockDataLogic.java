package main.java.logic;

import com.zoicapital.stockchartsfx.BarData;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.data.stock_data.StockDataAPI;
import main.java.util.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class StockDataLogic {
    // TODO attempt to make the service run in the logic layer and update the UI from here (potentially illtegalStateException so try with a dummy serivce first)

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

            String XAxisLabel = sdf.format(convertedData.getDateTime().getTime());
            series.getData().add(new XYChart.Data<>(XAxisLabel, convertedData.getOpen(), convertedData));
        }

        // can be added directly to the chart without further modifications
        return FXCollections.observableArrayList(series);
    }

    public static ObservableList<XYChart.Series<String, Number>> getLineChartData(){
        // values must be double and date must be gregorian calendar
        List<StockData> rawData = CurrentStockInformation.getInstance().getChartData();

        // convert the data to a chart ready format
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        SimpleDateFormat sdf = getXAxisFormat();
        for (StockData d : rawData) {

            String XAxisLabel = sdf.format( Date.from( d.getDateTime())); // Instant converted to Date
            series.getData().add(new XYChart.Data<>(XAxisLabel, d.getClose()));
        }

        // can be added directly to the chart without further modifications
        return FXCollections.observableArrayList(series);

    }

    /**
     * ensures no redundant data on X axis with proper date formatting
     * @return
     */
    private static SimpleDateFormat getXAxisFormat() {

        StockTimeSeriesType function = CurrentStockInformation.getInstance().getFunction();
        StockTimeSeriesIntradayInterval interval = CurrentStockInformation.getInstance().getInterval();

        if(function == StockTimeSeriesType.TIME_SERIES_INTRADAY){
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

    public static void updateCurrentStockViewedData() throws Exception{

        CurrentStockInformation.getInstance().updateStockData();
    }


    public static void setFunction(StockTimeSeriesType function){
        CurrentStockInformation.getInstance().setFunction(function);
    }

    public static void setStockSymbol(String symbol){
        CurrentStockInformation.getInstance().setStockSymbol(symbol);
    }

    public static void setInterval(StockTimeSeriesIntradayInterval interval){
        CurrentStockInformation.getInstance().setInterval(interval);
    }

    public static void setOutputSize(StockOutputSize outputSize){
        CurrentStockInformation.getInstance().setOutputSize(outputSize);
    }

    public static String getCurrentSymbol(){
        return CurrentStockInformation.getInstance().getStockSymbol();
    }

    public static SimpleObjectProperty<BigDecimal> getCurrentValueProperty(){
        return CurrentStockInformation.getInstance().currentValueProperty();
    }

    /**
     * updates the internal data and UI every set interval
     */
    public class StockDataUpdaterService extends ScheduledService<ObservableList<XYChart.Series<String, Number> > >{
        // TODO add listener for Investments Held and update
        private ChartType chartTypeToReturn = null; // should be candlestick or line

        private final StringProperty currentStockValue = new SimpleStringProperty(); // returns the current value as a string to be displayed

        private final  StringProperty currentStockSymbol = new SimpleStringProperty();

        public String getCurrentStockSymbol() {
            return currentStockSymbol.get();
        }

        void setCurrentStockSymbol(String value) {
            currentStockSymbol.set(value);
        }

        public StringProperty currentStockSymbolProperty() {
            return currentStockSymbol;
        }

        public String getCurrentStockValue() {
            return currentStockValue.get();
        }

        void setCurrentStockValue(String value) {
            currentStockValue.set(value);
        }

        public StringProperty currentStockValueProperty() {
            return currentStockValue;
        }

        public StockDataUpdaterService() {
            super();
            this.setPeriod(Duration.seconds(60));  // set default time of 60 second periods (update the chart every minute)
        }

        public void setChartTypeToReturn(ChartType type){
            chartTypeToReturn = type;
        }

        @Override
        protected Task<ObservableList<XYChart.Series<String, Number> > > createTask() {

            return new updateChartTask();
        }


        private class updateChartTask extends Task<ObservableList<XYChart.Series<String, Number> > >{

            @Override
            protected ObservableList<XYChart.Series<String, Number> > call() throws Exception {
                System.out.println("scheduled service StockDataUpdaterService is starting, type: " + chartTypeToReturn);
                // update internal model first with the latest information

                if(isCancelled()){
                    System.out.println("task was cancelled");
                    return null;
                }
                StockDataLogic.updateCurrentStockViewedData();

                // update investment values
                InvestmentsHeldLogic.updateInvestmentsHeld();

                // place updating of custom property in run later since UI cannot be updated from background thread
                Platform.runLater(() -> {
                    try {
                        setCurrentStockValue(CurrentStockInformation.getInstance().getCurrentValue().toString());
                    } catch (NullPointerException e){
                        // current stock value == null
                        setCurrentStockValue("N/A");
                    }


                    setCurrentStockSymbol(CurrentStockInformation.getInstance().getStockSymbol());
                });
                System.out.println("=============================================test===============================");
                if(isCancelled()){
                    System.out.println("task was cancelled");
                    return null;
                }
                // then return the correct dataType
                if (chartTypeToReturn == ChartType.CANDLESTICK){
                    System.out.println("returning candlestick chart data");
                    return StockDataLogic.getCandleStickChartData();

                }else if (chartTypeToReturn == ChartType.LINE){
                    System.out.println("returning line chart data");
                    return StockDataLogic.getLineChartData();

                }else {
                    System.out.println("error chart type");
                    throw new Exception("no valid chart type given to StockDataUpdaterService");
                }
            }
        }


    }

    public static Set<Company> getSetOfCompanies(){
        return StockDataAPI.getStockMarketCompanyList();
    }
}

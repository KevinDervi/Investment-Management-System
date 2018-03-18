package main.java.logic.investment_Analysis;

import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.data.stock_data.StockDataAPI;
import main.java.util.StockData;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class StockAnalysis {

    private ArrayList<StockDataPoint> dataSet = new ArrayList<>();

    public StockAnalysis(String predictionType) {

        StockTimeSeriesType function = getFunction(predictionType);
        String stockSymbol = CurrentStockInformation.getInstance().getStockSymbol();


        JSONObject stockData = null;
        try { // TODO make sure "GOOG" is not in the stock symbol area
            stockData = StockDataAPI.getStockData(function, "AAPL", null, StockOutputSize.FULL_HISTORY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createDataSetVolumeDifference(stockData, function);
        showChartData();

        createDataSetVolume(stockData, function);
        showChartData();
    }

    private void showChartData(){
        ScatterChart chart = new ScatterChart<>(new NumberAxis(), new NumberAxis());

        XYChart.Series<Number, Number> seriesBuy = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesSell = new XYChart.Series<>();

        // convert data to a javafx chart readable format
        for (StockDataPoint s : dataSet) {
            if(s.isShouldBuy()){
                seriesBuy.getData().add(new XYChart.Data<>(s.getxValue(), s.getyValue()));

            }else {
                seriesSell.getData().add(new XYChart.Data<>(s.getxValue(), s.getyValue()));
            }
        }
        seriesBuy.setName("Buy");
        seriesSell.setName("Sell");
        chart.getData().addAll(seriesBuy, seriesSell);
        System.out.println("seriesBuy size: " + seriesBuy.getData().size());
        System.out.println("seriesSell size: " + seriesSell.getData().size());
        Stage stage = new Stage();
        Scene scene  = new Scene(chart);
        stage.setScene(scene);
        stage.show();

    }

    private void createDataSetVolumeDifference(JSONObject JSONstockData, StockTimeSeriesType function) {

        // retrieve data and convert to array list
        ArrayList<StockDataPoint> stockDataArrayList = convertJSONToListWithVolumeDifferences(JSONstockData, function);

        // normalise the values
        //dataSet = normaliseData(stockDataArrayList);
        dataSet = stockDataArrayList;
    }

    private void createDataSetVolume(JSONObject JSONstockData, StockTimeSeriesType function) {
        dataSet = convertJSONToListWithVolume(JSONstockData, function);
    }


    private ArrayList<StockDataPoint> normaliseData(ArrayList<StockDataPoint> stockDataArrayList) {
        ArrayList<StockDataPoint> data = normaliseXValues(stockDataArrayList);

        data = normaliseYValues(data);

        return data;
    }

    private ArrayList<StockDataPoint> normaliseXValues(ArrayList<StockDataPoint> stockDataArrayList) {
        BigDecimal maxXValue = getMaxXValue(stockDataArrayList);
        BigDecimal minXValue = getMinXValue(stockDataArrayList);

        for (StockDataPoint s : stockDataArrayList) {

            // normalised value = (xi - min(x))/(max(x) - min(x))
            int scale = 5; // number of decimal places


            BigDecimal normalisedXValue = s.getxValue().subtract(minXValue).divide(maxXValue.subtract(minXValue), scale, RoundingMode.HALF_DOWN);

            s.setxValue(normalisedXValue);
        }

        return stockDataArrayList;
    }

    private ArrayList<StockDataPoint> normaliseYValues(ArrayList<StockDataPoint> stockDataArrayList) {
        BigDecimal maxYValue = getMaxYValue(stockDataArrayList);
        BigDecimal minYValue = getMinYValue(stockDataArrayList);

        for (StockDataPoint s : stockDataArrayList) {

            // normalised value = (yi - min(y))/(max(y) - min(y))
            int scale = 5; // number of decimal places


            BigDecimal normalisedYValue = s.getyValue().subtract(minYValue).divide(maxYValue.subtract(minYValue), scale, RoundingMode.HALF_DOWN);

            s.setyValue(normalisedYValue);
        }

        return stockDataArrayList;
    }

    private BigDecimal getMinXValue(ArrayList<StockDataPoint> stockDataArrayList) {
        BigDecimal minValue = stockDataArrayList.get(0).getxValue();

        for (StockDataPoint s : stockDataArrayList) {

            if(s.getxValue().compareTo(minValue) < 0){
                minValue = s.getxValue();
            }
        }

        return minValue;
    }

    private BigDecimal getMinYValue(ArrayList<StockDataPoint> stockDataArrayList) {
        BigDecimal minValue = stockDataArrayList.get(0).getyValue();

        for (StockDataPoint s : stockDataArrayList) {

            if(s.getyValue().compareTo(minValue) < 0){
                minValue = s.getyValue();
            }
        }

        return minValue;
    }

    private BigDecimal getMaxXValue(ArrayList<StockDataPoint> stockDataArrayList) {
        BigDecimal maxValue = stockDataArrayList.get(0).getxValue();

        for (StockDataPoint s : stockDataArrayList) {

            if(s.getxValue().compareTo(maxValue) > 0){
                maxValue = s.getxValue();
            }
        }

        return maxValue;
    }

    private BigDecimal getMaxYValue(ArrayList<StockDataPoint> stockDataArrayList) {
        BigDecimal maxValue = stockDataArrayList.get(0).getyValue();

        for (StockDataPoint s : stockDataArrayList) {

            if(s.getyValue().compareTo(maxValue) > 0){
                maxValue = s.getyValue();
            }
        }

        return maxValue;
    }

    private ArrayList<StockDataPoint> convertJSONToListWithVolumeDifferences(JSONObject JSONstockData, StockTimeSeriesType function) {
        ArrayList<StockData> orderedData = new ArrayList<>();

        try {
            orderedData = toArrayList(JSONstockData, function);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<StockDataPoint> arrayList = new ArrayList<>();

        BigDecimal lastVolume = BigDecimal.valueOf(orderedData.get(0).getVolume());

        for (StockData s : orderedData) {
            // convert data to StockData suitable format
            BigDecimal open = s.getOpen();
            BigDecimal close = s.getClose();
            BigDecimal volume = BigDecimal.valueOf(s.getVolume());

            // y value
            BigDecimal changeInVolume = volume.subtract(lastVolume);

            System.out.println("volume = " + volume);
            System.out.println("lastVolume = " + lastVolume);
            System.out.println("changeInVolume = " + changeInVolume);
            System.out.println();

            lastVolume = volume;

            boolean classificationShouldBuy;

            BigDecimal priceChange = close.subtract(open);

            // true if stock has increased in price and false if fallen or unchanged
            classificationShouldBuy = priceChange.compareTo(BigDecimal.ZERO) > 0;

            StockDataPoint dataPoint = new StockDataPoint(close, changeInVolume, classificationShouldBuy);

            arrayList.add(dataPoint);
        }

//
//        String StockDataTitle = getStockDataJSONTitle(function);
//        try {
//            // gets JSONObject of stock data from the API
//            JSONObject stockData = JSONstockData.getJSONObject(StockDataTitle);
//
//            // gets json keys
//            Iterator stockDataKeys = stockData.keys();
//
//            // ignore the first data point since we cannot determine the change in volume
//            // only extract the volume from it
//            String firstPointStringDate = (String) stockDataKeys.next();
//
//            JSONObject firstPointIndividualData = stockData.getJSONObject(firstPointStringDate);
//
//            String firstPointVolumeString = firstPointIndividualData.getString("5. volume");
//
//            // for the KNN classification to determine the change in volume for each
//            BigDecimal lastVolume = new BigDecimal(firstPointVolumeString);
//
//            // iterate through keys and add the information to an ArrayList
//            while(stockDataKeys.hasNext()){
//                String stringDate = (String) stockDataKeys.next();
//
//                JSONObject individualData = stockData.getJSONObject(stringDate);
//
//                String openString = individualData.getString("1. open");
//                String closeString = individualData.getString("4. close");
//                String volumeString = individualData.getString("5. volume");
//
//                // convert data to StockData suitable format
//                BigDecimal open = new BigDecimal(openString);
//                BigDecimal close = new BigDecimal(closeString);
//                BigDecimal volume = new BigDecimal(volumeString);
//
//                // y value
//                BigDecimal changeInVolume = volume.subtract(lastVolume);
//                System.out.println("volume = " + volume);
//                System.out.println("lastVolume = " + lastVolume);
//                System.out.println("changeInVolume = " + changeInVolume);
//                System.out.println();
//                lastVolume = volume;
//
//                boolean classificationShouldBuy;
//
//                BigDecimal priceChange = close.subtract(open);
//
//                // true if stock has increased in price and false if fallen or unchanged
//                classificationShouldBuy = priceChange.compareTo(BigDecimal.ZERO) > 0;
//
//                StockDataPoint dataPoint = new StockDataPoint(close, changeInVolume, classificationShouldBuy);
//
//                arrayList.add(dataPoint);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        return arrayList;
    }

    private ArrayList<StockDataPoint> convertJSONToListWithVolume(JSONObject JSONstockData, StockTimeSeriesType function) {
        ArrayList<StockDataPoint> arrayList = new ArrayList<>();


        String StockDataTitle = getStockDataJSONTitle(function);
        try {
            // gets JSONObject of stock data from the API
            JSONObject stockData = JSONstockData.getJSONObject(StockDataTitle);

            // gets json keys
            Iterator stockDataKeys = stockData.keys();

            // iterate through keys and add the information to an ArrayList
            while(stockDataKeys.hasNext()){
                String stringDate = (String) stockDataKeys.next();

                JSONObject individualData = stockData.getJSONObject(stringDate);

                String openString = individualData.getString("1. open");
                String closeString = individualData.getString("4. close");
                String volumeString = individualData.getString("5. volume");

                // convert data to StockData suitable format
                BigDecimal open = new BigDecimal(openString);
                BigDecimal close = new BigDecimal(closeString);
                BigDecimal volume = new BigDecimal(volumeString);

                boolean classificationShouldBuy;

                BigDecimal priceChange = close.subtract(open);

                // true if stock has increased in price and false if fallen or unchanged
                classificationShouldBuy = priceChange.compareTo(BigDecimal.ZERO) > 0;

                StockDataPoint dataPoint = new StockDataPoint(close, volume, classificationShouldBuy);

                arrayList.add(dataPoint);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return arrayList;
    }


    // ================== UTILITY METHODS ==================================================
    private ArrayList<StockData> toArrayList(JSONObject data, StockTimeSeriesType function) throws JSONException {
        ArrayList<StockData> stockDataArrayList = new ArrayList<>();

        String StockDataTitle = getStockDataJSONTitle(function);

        // gets JSONObject of stock data from the API
        JSONObject stockData = data.getJSONObject(StockDataTitle);

        // gets json keys
        Iterator stockDataKeys = stockData.keys();

        // iterate through keys and add the information to an ArrayList
        while(stockDataKeys.hasNext()){
            String stringDate = (String) stockDataKeys.next();

            JSONObject individualData = stockData.getJSONObject(stringDate);

            String openString = individualData.getString("1. open");
            String highString = individualData.getString("2. high");
            String lowString = individualData.getString("3. low");
            String closeString = individualData.getString("4. close");
            String volumeString = individualData.getString("5. volume");

            // convert data to StockData suitable format
            BigDecimal open = new BigDecimal(openString);
            BigDecimal high = new BigDecimal(highString);
            BigDecimal low = new BigDecimal(lowString);
            BigDecimal close = new BigDecimal(closeString);
            Long volume = Long.parseLong(volumeString,10);

            Instant dateTime = this.toInstant(stringDate);


            StockData individualStockData = new StockData(open, high, low, close, volume, dateTime);

            stockDataArrayList.add(individualStockData);
        }

        // order the array list since it is in random order
        // list ordered from oldest time at index 0 to latest time (for chart display)
        stockDataArrayList.sort(Comparator.comparing(StockData::getDateTime));


        // finally add the data retrieved to
        return stockDataArrayList;

    }

    private Instant toInstant(String dateTime){
        StringBuilder dateTimeBuilder = new StringBuilder(dateTime);
        if (dateTime.length() < 12){
            dateTimeBuilder.append(" 00:00:00");
        }

        // T seperates the date from the time when parsing and Z indicated zero time offset
        dateTimeBuilder.setCharAt(10, 'T');
        dateTimeBuilder.append('Z');

        dateTime = dateTimeBuilder.toString();


        return  Instant.parse(dateTime);
    }

    /**
     * returns the key of the json object that contains the stock data in the Alpha Vantage API
     * @param function
     * @return
     */
    private String getStockDataJSONTitle(StockTimeSeriesType function){
        // works for only daily, weekly and monthly functions
        if(function == StockTimeSeriesType.TIME_SERIES_DAILY) return "Time Series (Daily)";
        else if (function == StockTimeSeriesType.TIME_SERIES_WEEKLY) return "Weekly Time Series";
        return  "Monthly Time Series"; // Monthly
    }

    private StockTimeSeriesType getFunction(String predictionType) {
        StockTimeSeriesType functionToReturn;
        switch (predictionType){
            case "1-Day":
                functionToReturn = StockTimeSeriesType.TIME_SERIES_DAILY;
                break;

            case "1-Week":
                functionToReturn = StockTimeSeriesType.TIME_SERIES_WEEKLY;
                break;

            case "1-Month":
                functionToReturn = StockTimeSeriesType.TIME_SERIES_MONTHLY;
                break;

            default:
                functionToReturn = StockTimeSeriesType.TIME_SERIES_DAILY;
        }

        return functionToReturn;

    }

    /**
     * represents a data point co-ordinates and its classification
     */
    private class StockDataPoint{

        private BigDecimal xValue; // the price of the stock
        private BigDecimal yValue; // volume / change in volume


        private boolean shouldBuy; // if the stock data point has increased in value (i.e. the user should buy)

        /**
         * Creates a point on the graph for KNN stock analysis
         *
         * @param xValue   the price of the stock at a given point
         * @param yValue the change in volume compared to the last point
         */
        StockDataPoint(BigDecimal xValue, BigDecimal yValue, boolean shouldBuy) {
            this.xValue = xValue;
            this.yValue = yValue;
            this.shouldBuy = shouldBuy;
        }

        BigDecimal getxValue() {
            return xValue;
        }

        BigDecimal getyValue() {
            return yValue;
        }

        boolean isShouldBuy() {
            return shouldBuy;
        }

        void setxValue(BigDecimal xValue) {
            this.xValue = xValue;
        }

        void setyValue(BigDecimal yValue) {
            this.yValue = yValue;
        }
    }
}

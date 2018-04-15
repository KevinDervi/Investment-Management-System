package main.java.logic.investment_Analysis;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.data.stock_data.StockDataAPI;
import main.java.util.StockData;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StockAnalysis {

    private final ArrayList<StockDataPoint> dataSet = new ArrayList<>();
    private final ArrayList<StockDataPoint> TestdataSet = new ArrayList<>();
    private final StockTimeSeriesType function;
    private final String stockSymbol;

    // pick the last n values to be in the prediction
    private int lastNValues = 3;

    private StockDataPoint predictionPoint;
    private Boolean predictionClassification;
    private Double accuracy;

    private int truePositives;
    private int trueNegatives;
    private int falsePositives;
    private int falseNegatives;

    public StockAnalysis(String predictionType) {

        function = getFunction(predictionType);
        stockSymbol = CurrentStockInformation.getInstance().getStockSymbol();

    }

    public Service<AnalysisResult> getStockAnalysisService() {
        return new StockAnalysisService();
    }

    public void performKNN(int kValue){

        resetResults();

        predictionPoint = getPrediction();
        System.out.println("================================================");
        System.out.println("for K Value: " + kValue);
        // KNN performed on training data
        for (int i = lastNValues; i < dataSet.size(); ++i) {
            // get the location to be categorised
            performKNNOnDataSetPoint(i, kValue);

        }

        // perform KNN on our actual prediction of the next stock
        PredictionClassification(kValue);

        printStatistics();

    }

    private void resetResults() {
        predictionPoint = null;
        predictionClassification = null;
        accuracy = null;

        truePositives = 0;
        trueNegatives = 0;
        falsePositives = 0;
        falseNegatives = 0;
    }

    private void printStatistics() {
        System.out.println();


        System.out.println("prediction Point: " + predictionPoint.getxValue() + ", " + predictionPoint.getyValue());
        System.out.println("predictionClassification = " + predictionClassification);

        System.out.println();

        System.out.println("total dataset size: " + dataSet.size());
        System.out.println("truePositives = " + truePositives);
        System.out.println("trueNegatives = " + trueNegatives);
        System.out.println("falsePositives = " + falsePositives);
        System.out.println("falseNegatives = " + falseNegatives);

        System.out.println();
        System.out.println("accuracy = " + accuracy);

        System.out.println();
    }

    /**
     * get the classification of our prediction
     * @param kValue
     * @return
     */
    private void PredictionClassification(int kValue) {
        // sort the data based on manhattan distance
        ArrayList<StockDataPoint> sortedDataSet = sortByDistance(predictionPoint);

        // get the KNN for our prediction
        List<StockDataPoint> kNNList= sortedDataSet.subList(sortedDataSet.size()-2-kValue, sortedDataSet.size()-2);

        // set the classification for our prediction
        predictionClassification = getClassificationPrediction(kNNList);

        // set the accuracy for our prediction
        accuracy = (( (double) truePositives + trueNegatives) / (truePositives + trueNegatives + falseNegatives + falsePositives)) * 100.0;
    }

    /**
     * get classification when iterating through training data
     * @param pointLocation
     * @param kValue
     */
    private void performKNNOnDataSetPoint(int pointLocation, int kValue) {
        // get our prediction for the training data
        StockDataPoint s = getPredictionForDataSetPoint(pointLocation);

        // add to test dataset
        if (TestdataSet.size() < dataSet.size()){
            TestdataSet.add(s);
        }
        // sort the data based on manhattan distance
        ArrayList<StockDataPoint> sortedDataSet = sortByDistance(s);


        // get the KNN for s
        List<StockDataPoint> kNNList= sortedDataSet.subList(1, 1 + kValue);

        // get the prediction for s
        boolean tempPredictionClassification = getClassificationPrediction(kNNList);

        // record prediction
        recordPrediction(s, tempPredictionClassification);
    }

    /**
     * records if the prediction is correct
     * @param s
     * @param predictionClassification
     */
    private void recordPrediction(StockDataPoint s, boolean predictionClassification) {
        boolean actualclassification = s.shouldBuy();

        // adds values based on true/false positives/negatives
        if(actualclassification){
            if (predictionClassification){
                truePositives += 1;
            } else {
                falsePositives += 1;
            }
        }else{
            if (predictionClassification){
                falseNegatives += 1;
            }else {
                trueNegatives += 1;
            }
        }
    }

    private boolean getClassificationPrediction(List<StockDataPoint> kNNList) {

        int buyCount = 0;
        int sellCount = 0;

        // count each classification and return the highest one
        for (StockDataPoint s : kNNList) {
            if (s.shouldBuy()){
                buyCount += 1;
            } else{
                sellCount +=1;
            }
        }

        return buyCount > sellCount;
    }

    private ArrayList<StockDataPoint> sortByDistance(StockDataPoint s) {
        // create a temporary list to sort by distance from point s

        ArrayList<StockDataPoint> tempList = new ArrayList<>(dataSet);

        // sort the list based on comparator
        tempList.sort(stockDataPointComparatorManhattan(s));

        // return sortedList
        return tempList;
    }

    private Comparator<StockDataPoint> stockDataPointComparatorManhattan(StockDataPoint s)
    {
        final StockDataPoint finalP = new StockDataPoint(s.getxValue(), s.getyValue(), s.shouldBuy());
        return (s1, s2) -> {
            BigDecimal s1Compared = s1.getManhattanDistance(finalP);
            BigDecimal s2Compared = s2.getManhattanDistance(finalP);
            return s1Compared.compareTo(s2Compared);
        };
    }

    private Comparator<StockDataPoint> stockDataPointComparatorEuclidean(StockDataPoint s)
    {
        final StockDataPoint finalP = new StockDataPoint(s.getxValue(), s.getyValue(), s.shouldBuy());
        return (s1, s2) -> {
            BigDecimal s1Compared = s1.getEuclideanDistance(finalP);
            BigDecimal s2Compared = s2.getEuclideanDistance(finalP);
            return s1Compared.compareTo(s2Compared);
        };
    }

    /**
     * gets a prediction based on the mean of the latest 3 values from the dataset
     * @return
     */
    private StockDataPoint getPrediction() {


        int nthValueLocation = dataSet.size() - 1 - lastNValues;


        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // iterate through the last n values and add to total
        for (int i = dataSet.size()- 1; i > nthValueLocation; --i){
            StockDataPoint currentPoint = dataSet.get(i);

            totalVolume = currentPoint.getyValue().add(totalVolume);
            totalPrice = currentPoint.getxValue().add(totalPrice);
        }

        BigDecimal meanValue = BigDecimal.valueOf(lastNValues);

        // find the mean of both x and y axis of the last n values
        BigDecimal meanVolume = totalVolume.divide(meanValue, 5, RoundingMode.HALF_DOWN);
        BigDecimal meanPrice = totalPrice.divide(meanValue, 5, RoundingMode.HALF_DOWN);

        // return the mean prediction
        return new StockDataPoint(meanPrice, meanVolume, null);

    }

    private StockDataPoint getPredictionForDataSetPoint(int predictionLocation){

        BigDecimal totalVolume = BigDecimal.ZERO;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // iterate through the last n values and add to total
        for (int i = predictionLocation - lastNValues; i < predictionLocation; ++i){
            StockDataPoint currentPoint = dataSet.get(i);

            totalVolume = currentPoint.getyValue().add(totalVolume);
            totalPrice = currentPoint.getxValue().add(totalPrice);
        }

        BigDecimal meanValue = BigDecimal.valueOf(lastNValues);

        // find the mean of both x and y axis of the last n values
        BigDecimal meanVolume = totalVolume.divide(meanValue, 5, RoundingMode.HALF_DOWN);
        BigDecimal meanPrice = totalPrice.divide(meanValue, 5, RoundingMode.HALF_DOWN);


        /**
         * testing
         */
        StockDataPoint currentPoint = dataSet.get(predictionLocation - 1);

        BigDecimal latestVolume = currentPoint.getyValue();
        BigDecimal latestPrice = currentPoint.getxValue();

        BigDecimal differenceVolume = latestVolume.subtract(meanVolume);
        BigDecimal differencePrice = latestPrice.subtract(meanPrice);

        BigDecimal predictionVolume = latestVolume.add(differenceVolume);
        BigDecimal predictionPrice= latestPrice.add(differencePrice);

        // ensure no negative price predictions
        if (predictionPrice.compareTo(BigDecimal.ZERO) < 0){
            predictionPrice = BigDecimal.ZERO;
        }

        // actual classification (classification of the next value)
        boolean actualClassification = dataSet.get(predictionLocation).shouldBuy();

        // return the mean prediction
        //return new StockDataPoint(meanPrice, meanVolume, actualClassification);
        return new StockDataPoint(predictionPrice, predictionVolume, actualClassification);
    }

    private void showChartData(){
        ScatterChart chart = new ScatterChart<>(new NumberAxis(), new NumberAxis());

        XYChart.Series<Number, Number> seriesBuy = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesSell = new XYChart.Series<>();

        // convert data to a javafx chart readable format
        for (StockDataPoint s : dataSet) {
            if(s.shouldBuy()){
                seriesBuy.getData().add(new XYChart.Data<>(s.getxValue(), s.getyValue()));

            }else {
                seriesSell.getData().add(new XYChart.Data<>(s.getxValue(), s.getyValue()));
            }
        }

        seriesBuy.setName("Buy");
        seriesSell.setName("Sell");
        chart.setTitle(stockSymbol + " " + function);
        chart.getXAxis().setLabel("Price");

        chart.getYAxis().setLabel("Volume difference from previous value");


        chart.getStylesheets().add(getClass().getResource("/main/resources/css_styles/stock_analysis_graph.css").toExternalForm());


        chart.getData().addAll(seriesBuy, seriesSell);
        chart.setAnimated(false);

        Stage stage = new Stage();
        Scene scene  = new Scene(chart);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


        // save the graph as an image file
        WritableImage snapShot = scene.snapshot(null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(stockSymbol + "_" + function + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTestDataset(){
        ScatterChart chart = new ScatterChart<>(new NumberAxis(), new NumberAxis());

        XYChart.Series<Number, Number> seriesBuy = new XYChart.Series<>();
        XYChart.Series<Number, Number> seriesSell = new XYChart.Series<>();

        // convert data to a javafx chart readable format
        for (StockDataPoint s : TestdataSet) {
            if(s.shouldBuy()){
                seriesBuy.getData().add(new XYChart.Data<>(s.getxValue(), s.getyValue()));

            }else {
                seriesSell.getData().add(new XYChart.Data<>(s.getxValue(), s.getyValue()));
            }
        }

        seriesBuy.setName("Buy");
        seriesSell.setName("Sell");
        chart.setTitle(stockSymbol + " " + function + " " + "Test Dataset");
        chart.getXAxis().setLabel("Price");

        chart.getYAxis().setLabel("Volume difference from previous value");


        chart.getStylesheets().add(getClass().getResource("/main/resources/css_styles/stock_analysis_graph.css").toExternalForm());


        chart.getData().addAll(seriesBuy, seriesSell);
        chart.setAnimated(false);

        Stage stage = new Stage();
        Scene scene  = new Scene(chart);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


        // save the graph as an image file
        WritableImage snapShot = scene.snapshot(null);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(snapShot, null), "png", new File(stockSymbol + "_" + function + "_" +  "TestDataset" + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createDataSetVolumeDifference(JSONObject JSONstockData) {

        // retrieve data and convert to array list
        ArrayList<StockDataPoint> stockDataArrayList = convertJSONToListWithVolumeDifferences(JSONstockData);

        // normalise the values
        //dataSet.addAll(normaliseData(stockDataArrayList));
        dataSet.addAll(stockDataArrayList);
    }

    private void createDataSetVolume(JSONObject JSONstockData) {
        dataSet.addAll(convertJSONToListWithVolume(JSONstockData));
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

    private ArrayList<StockDataPoint> convertJSONToListWithVolumeDifferences(JSONObject JSONstockData) {
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

            lastVolume = volume;

            boolean classificationShouldBuy;

            BigDecimal priceChange = close.subtract(open);

            // true if stock has increased in price and false if fallen or unchanged
            classificationShouldBuy = priceChange.compareTo(BigDecimal.ZERO) > 0;

            StockDataPoint dataPoint = new StockDataPoint(close, changeInVolume, classificationShouldBuy);

            arrayList.add(dataPoint);
        }


        return arrayList;
    }

    private ArrayList<StockDataPoint> convertJSONToListWithVolume(JSONObject JSONstockData) {
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


        private Boolean shouldBuy; // if the stock data point has increased in value (i.e. the user should buy)

        /**
         * Creates a point on the graph for KNN stock analysis
         *
         * @param xValue   the price of the stock at a given point
         * @param yValue the change in volume compared to the last point
         */
        StockDataPoint(BigDecimal xValue, BigDecimal yValue, Boolean shouldBuy) {
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

        Boolean shouldBuy() {
            return shouldBuy;
        }

        void setxValue(BigDecimal xValue) {
            this.xValue = xValue;
        }

        void setyValue(BigDecimal yValue) {
            this.yValue = yValue;
        }

        BigDecimal getManhattanDistance(StockDataPoint s){
            BigDecimal xDifference = xValue.subtract(s.getxValue()).abs();
            BigDecimal yDifference = yValue.subtract(s.getyValue()).abs();

            return xDifference.add(yDifference);
        }

        BigDecimal getEuclideanDistance(StockDataPoint s){
            // return sqrt((x - x2)^2 + (y - y2)^2)
            BigDecimal xDifference = xValue.subtract(s.getxValue());
            BigDecimal yDifference = yValue.subtract(s.getyValue());

            BigDecimal xDifferenceSquared = xDifference.pow(2);
            BigDecimal yDifferenceSquared = yDifference.pow(2);

            BigDecimal xSquaredPlusYSquared = xDifferenceSquared.add(yDifferenceSquared);

            return xSquaredPlusYSquared.sqrt(MathContext.DECIMAL64);
        }
    }


    public class StockAnalysisService extends Service<AnalysisResult> {

        @Override
        protected Task<AnalysisResult> createTask() {
            return new StockAnalysisTask();
        }


        private class StockAnalysisTask extends Task<AnalysisResult> {

            @Override
            protected AnalysisResult call() throws Exception {
                updateProgress(0.0, 9);
                //get and setup dataset for analysis
                JSONObject stockData = null;
                try {
                    stockData = StockDataAPI.getStockData(function, stockSymbol, null, StockOutputSize.FULL_HISTORY);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                createDataSetVolumeDifference(stockData);
                //createDataSetVolume(stockData);

                //Platform.runLater(StockAnalysis.this::showChartData);


                // create initial best result of 0 accuracy
                AnalysisResult bestResult = new AnalysisResult(false, 0.0);
                int bestK = -1;

                //record total time taken
                long initialStartTime = System.currentTimeMillis();




                // iterate in steps of 2 from 1 - 9 (inclusive) for the k value
                int maxK = 10;
                for (int k = 1; k < maxK; k+=2){
                    if(Thread.interrupted()){
                        return null;
                    }


                    long startTime = System.currentTimeMillis();

                    performKNN(k);
                    AnalysisResult result = new AnalysisResult(predictionClassification, accuracy);


                    // if result is less tahn 50% then invert the higher accuracy
                    if (accuracy < 50){
                        result = result.invert();
                    }

                    if (result.getAccuracy() > bestResult.getAccuracy()){
                        bestResult = result;
                        bestK = k;
                    }

                    System.out.println("time taken for k = " + k + " : " + (System.currentTimeMillis() - startTime)/1000.0 + "s");


                    updateProgress(k, maxK);
                }

                System.out.println("total time taken: " + (System.currentTimeMillis() - initialStartTime)/1000.0 + "s");
                System.out.println("bestResult = " + bestResult);
                System.out.println("bestK = " + bestK);

                if(Thread.interrupted()){
                    return null;
                }

                //Platform.runLater(StockAnalysis.this::showTestDataset);

                // request garbage collection
                System.gc();

                return bestResult;
            }
        }
    }
}

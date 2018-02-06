package main.java.data.internal_model;

import com.zoicapital.stockchartsfx.BarData;
import main.java.data.stock_data.StockDataAPI;
import main.java.util.StockData;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import main.java.util.StockTimeSeriesIntradayInterval;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class CurrentStockInformation {
    // TODO holds all the information on the current stock that the user is viewing

    private boolean stockMarketsClosed = false;

    private ArrayList<StockData> chartData;

    // data required for getting stock information
    private StockTimeSeriesType function;
    private String stockSymbol;
    private StockTimeSeriesIntradayInterval interval;
    private StockOutputSize outputSize;

    private static CurrentStockInformation Instance;

    private CurrentStockInformation(){}

    public static CurrentStockInformation getInstance() {
        if(Instance == null){
            Instance = new CurrentStockInformation();
        }
        return Instance;
    }

    public void setFunction(StockTimeSeriesType function) {
        this.function = function;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setInterval(StockTimeSeriesIntradayInterval interval) {
        this.interval = interval;
    }

    public void setOutputSize(StockOutputSize outputSize) {
        this.outputSize = outputSize;
    }

    public void setInstance(CurrentStockInformation instance) {
        Instance = instance;
    }

    public ArrayList<StockData> getChartData() {
        return chartData;
    }

    // updates the internal model with the latest stock data
    public void updateChartData() throws Exception {
        // TODO handle IO Exception 503 (server busy) by trying again in a few seconds
        // TODO also update if the stock markets are closed and ensure the user in unable to buy and sell
        // ensure there are no null values before data is retrieved
        assert function != null;
        assert stockSymbol != null;

        if(function == StockTimeSeriesType.TIME_SERIES_INTRADAY){
            assert interval != null;
        }
        assert outputSize != null;

        // get json data
        JSONObject JSONStockdata = null;
        try {
            JSONStockdata = StockDataAPI.getStockData(function, stockSymbol, interval, outputSize);
        } catch (IOException e){
            // 503 response code, try again in 2 seconds
            System.out.println("503 responde code, waiting 2 seconds and trying again");
            Thread.sleep(2000);
            // TODO limit to 3 tries only or may be infinite
            updateChartData();
        }

        System.out.println(JSONStockdata.toString(4));
        if (JSONStockdata != null) toArrayList(JSONStockdata);



        // convert json to arraylist

    }

    private void toArrayList(JSONObject data) throws JSONException {
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
        chartData = stockDataArrayList;

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
        if(function == StockTimeSeriesType.TIME_SERIES_INTRADAY){
            return  "Time Series (" + interval +")";
        } else if(function == StockTimeSeriesType.TIME_SERIES_DAILY){
            return  "Time Series (Daily)";
        }else if (function == StockTimeSeriesType.TIME_SERIES_WEEKLY){
            return  "Weekly Time Series";
        }else { // Monthly
            return  "Monthly Time Series";
        }
    }
}

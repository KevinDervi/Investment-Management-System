package main.java.data.internal_model;

import com.zoicapital.stockchartsfx.BarData;
import main.java.data.stock_data.StockDataAPI;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import main.java.util.StockTimeSeriesIntradayInterval;
import org.json.JSONObject;

import java.util.ArrayList;

public class CurrentStockInformation {
    // TODO holds all the information on the current stock that the user is viewing

    private ArrayList<BarData> chartData;

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

    public ArrayList<BarData> getChartData() {
        return chartData;
    }

    // updates the internal model with the latest stock data
    public void updateChartData() throws Exception {
        // TODO handle IO Exception 503 (server busy) by trying again in a few seconds
        // ensure there are no null values before data is retrieved
        assert function != null;
        assert stockSymbol != null;

        if(function == StockTimeSeriesType.TIME_SERIES_INTRADAY){
            assert interval != null;
        }
        assert outputSize != null;

        // get json data
        JSONObject JSONStockdata = StockDataAPI.getStockData(function, stockSymbol, interval, outputSize);
        System.out.println(JSONStockdata.toString(4));

        System.out.println(JSONStockdata.getJSONObject("Time Series (1min)").length());
        BarData barData = new BarData();

        // convert json to arraylist

    }
}

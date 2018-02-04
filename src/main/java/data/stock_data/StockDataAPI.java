package main.java.data.stock_data;

import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeries;
import main.java.util.StockTimeSeriesIntradayInterval;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;

public class StockDataAPI {

    /**
     *
     * @param function time series function
     * @param stockSymbol US based stock symbol
     * @param interval time interval for Intraday stock data only (null otherwise)
     * @param outputSize data output size (compact for real time)
     * @return JSONObject that contains stock data
     * @throws Exception
     */
    public static JSONObject getStockData(StockTimeSeries function, String stockSymbol, StockTimeSeriesIntradayInterval interval, StockOutputSize outputSize) throws Exception {
        if (function != StockTimeSeries.TIME_SERIES_INTRADAY){
            assert interval == null;
        }

        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=GOOG&interval=15min&outputsize=compact&apikey=R99UN2NEI0VJS5ND";

        // simplest way to get json object from url
        URL stockURL = new URL(url);
        JSONTokener tokener = new JSONTokener(stockURL.openStream());
        JSONObject json = new JSONObject(tokener);
        System.out.println(json.toString(4));
        return null;
    }
}
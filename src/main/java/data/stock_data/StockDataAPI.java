package main.java.data.stock_data;

import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import main.java.util.StockTimeSeriesIntradayInterval;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URL;

public class StockDataAPI {

    private static final String API_KEY = "R99UN2NEI0VJS5ND";

    /**
     * returns the raw stock data received as a JSONObject
     * @param function time series function
     * @param stockSymbol US based stock symbol
     * @param interval time interval for Intraday stock data only (null otherwise)
     * @param outputSize data output size (compact for real time)
     * @return JSONObject that contains stock data
     * @throws Exception
     */
    public static JSONObject getStockData(StockTimeSeriesType function, String stockSymbol, StockTimeSeriesIntradayInterval interval, StockOutputSize outputSize) throws Exception {
        if (function != StockTimeSeriesType.TIME_SERIES_INTRADAY){
            assert interval == null;
        }else {
            assert interval != null;
            System.out.println(interval);
        }

        String url = "https://www.alphavantage.co/query?";
        url += "symbol=" + stockSymbol;
        url += "&";
        url += "function=" + function;
        url += "&";

        // interval only applies when the time series in intraday
        if(function == StockTimeSeriesType.TIME_SERIES_INTRADAY){
            url += "interval=" + interval;
            url += "&";
        }


        url += "outputsize=" + outputSize;
        url += "&";
        url += "apikey=" + API_KEY;

        System.out.println(url);

        // simplest way to get json object from url
        URL stockURL = new URL(url);
        //System.out.println(stockURL);
        JSONTokener tokener = new JSONTokener(stockURL.openStream());

        JSONObject json = new JSONObject(tokener);


        // look for an error message if none is found then return the data
        try{
            json.getString("Error Message");
        }catch (Exception e){
            return json;
        }

        // TODO store stock data as a local file and return that instead if the url fails
        // else throw exception
        System.out.println(json.toString(4)); // print out error message
        throw new Exception("error with api call");
    }
}
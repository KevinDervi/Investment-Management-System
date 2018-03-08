package main.java.data.stock_data;

import main.java.util.Company;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import main.java.util.StockTimeSeriesIntradayInterval;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            // TODO if error then return JSON data from a local file
        }catch (Exception e){
            System.out.println("stock data retrieval successful");
            return json;
        }

        // TODO store stock data as a local file and return that instead if the url fails
        // else throw exception
        System.out.println(json.toString(4)); // print out error message
        throw new Exception("error with api call");
    }


    public static JSONObject getSingleLatestStockData(String symbol) throws Exception{
        String url = "https://www.alphavantage.co/query?";
        url += "function=BATCH_STOCK_QUOTES";
        url += "&symbols=";
        url += symbol;
        url += "&apikey=" + API_KEY;


        URL stockURL = new URL(url);
        JSONTokener tokener = new JSONTokener(stockURL.openStream());

        JSONObject json = new JSONObject(tokener);


        // look for an error message if none is found then return the data
        try{
            json.getString("Error Message");

            // TODO if error then return JSON data from a local file
        }catch (Exception e){
            System.out.println("stock data retrieval successful");
            return json;
        }
        return null;
    }

    public static List<JSONObject> getmultipleLatestStockData(HashSet<String> symbols){
        return null;
    }

    public static Set<Company> getStockMarketCompanyList(){
        Set<Company> companies = new HashSet<>();

        companies.addAll(getNASDAQCompanies());
        companies.addAll(getNYSECompanies());

        return companies;
    }

    private static Set<Company> getNASDAQCompanies(){
        Set<Company> companies = new HashSet<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(StockDataAPI.class.getResourceAsStream("/main/resources/company_data/companylist NASDAQ.csv")));

            // ignore first line
            bufferedReader.readLine();

            companies = readFromCompanyListCSV(companies, bufferedReader);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return companies;
    }

    private static Set<Company> getNYSECompanies(){
        Set<Company> companies = new HashSet<>();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(StockDataAPI.class.getResourceAsStream("/main/resources/company_data/companylist NYSE.csv")));

            // ignore first line
            bufferedReader.readLine();

            companies = readFromCompanyListCSV(companies, bufferedReader);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return companies;
    }

    private static Set<Company> readFromCompanyListCSV(Set<Company> companies, BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {

            String[] companyLine = line.split(","); // separated by commas

            String companySymbol = companyLine[0].replace("\"", "");
            String companyName = companyLine[1].replace("\"", "");

            Company company = new Company(companySymbol, companyName);

            companies.add(company);

        }

        return companies;
    }

}
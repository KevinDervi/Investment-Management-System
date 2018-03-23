package main.java.data.stock_data;

import main.java.util.Company;
import main.java.util.StockOutputSize;
import main.java.util.StockTimeSeriesType;
import main.java.util.StockTimeSeriesIntradayInterval;
import org.json.JSONArray;
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
        JSONTokener tokener;
        JSONObject json;

        try {
            // try block checks for server or user errors (no data retrieved)
            tokener = new JSONTokener(stockURL.openStream());
            json = new JSONObject(tokener);
        }catch (Exception e){
            System.out.println("503 error in StockDataAPI");
            return getStockDataFromBackup(stockSymbol, function, interval, outputSize);
        }


        try{
            // look for an error message if found then simply get data from backup
            json.getString("Error Message");

        }catch (Exception e){
            System.out.println("stock data retrieval successful");
            addToBackupStockData(json, stockSymbol, function, interval, outputSize);


        }

        // finally return the relevant data (or default if errors)
        return getStockDataFromBackup(stockSymbol, function, interval, outputSize);
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

    public static JSONArray getmultipleLatestStockData(HashSet<String> symbols) throws Exception{
        StringBuilder url = new StringBuilder("https://www.alphavantage.co/query?");
        url.append("function=BATCH_STOCK_QUOTES");
        url.append("&symbols=");

        for (String symbol: symbols) {
            url.append(symbol + ",");
        }

        // delete the last ","
        url.deleteCharAt(url.length() - 1);

        url.append("&apikey=" + API_KEY);


        URL stockURL = new URL(url.toString());
        System.out.println(stockURL);

        JSONTokener tokener;
        try {
            tokener = new JSONTokener(stockURL.openStream());

        }catch (Exception e){
            // internet exception
            return null;
        }

        JSONObject json = new JSONObject(tokener);


        // look for an error message if none is found then return the data
        try{
            json.getString("Error Message");

        }catch (Exception e){
            System.out.println("stock data retrieval successful");
            return json.getJSONArray("Stock Quotes");
        }
        return null;
    }

    // ======================================= utilities =======================================
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

    /**
     * retrieve backup data if API fails
     * @param symbol
     * @param function
     * @param interval
     * @param outputSize
     * @return
     * @throws Exception
     */
    private static JSONObject getStockDataFromBackup(String symbol, StockTimeSeriesType function, StockTimeSeriesIntradayInterval interval, StockOutputSize outputSize) throws Exception {

        try {
            String filename = "src/main/resources/backup_stock_data/"; // relative file path
            filename += symbol + function;
            if (interval != null){
                filename += interval;
            }
            filename += outputSize;

            filename += ".json";

            // try to find the backup file we want
            JSONTokener tokener = new JSONTokener(new FileReader(filename));

            // return as JSON Object
            return new JSONObject(tokener);

        } catch (FileNotFoundException e) {

            System.out.println("backup stock data not found. returning default data");

            // create the url of the default data we want to return
            String defaultStockDataURL = "src/main/resources/backup_stock_data/default";

            defaultStockDataURL += function;

            if (interval != null){
                defaultStockDataURL += interval;
            }
            defaultStockDataURL += outputSize;

            defaultStockDataURL += ".json";

            // read from and return the default data as a json object
            JSONTokener backupTokener = new JSONTokener(new FileReader(defaultStockDataURL));

            return new JSONObject(backupTokener);

        }

    }

    private static void addToBackupStockData(JSONObject obj , String symbol, StockTimeSeriesType function, StockTimeSeriesIntradayInterval interval, StockOutputSize outputSize){
        String filename = symbol + function;
        if (interval != null){
            filename += interval;
        }
        filename += outputSize;


        // try to create file or overwrite one if it already exists
        try {


            FileWriter fileWriter = new FileWriter("src/main/resources/backup_stock_data/" + filename + ".json");

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(obj.toString(4));
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
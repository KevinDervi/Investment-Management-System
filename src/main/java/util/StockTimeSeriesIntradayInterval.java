package main.java.util;

public enum StockTimeSeriesIntradayInterval {
    T1("1min"),
    T5("5min"),
    T15("15min"),
    T30("30min"),
    T60("60min");


    private String interval;

    private StockTimeSeriesIntradayInterval(String interval){
        this.interval = interval;
    }

    @Override
    public String toString() {
        return this.interval;
    }
}

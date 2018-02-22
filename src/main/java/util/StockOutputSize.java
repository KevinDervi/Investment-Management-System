package main.java.util;

public enum StockOutputSize {

    REAL_TIME("compact"),
    FULL_HISTORY("full");


    String outputSize;

    private StockOutputSize(String outputSize) {
        this.outputSize = outputSize;
    }

    @Override
    public String toString() {
        return this.outputSize;
    }
}

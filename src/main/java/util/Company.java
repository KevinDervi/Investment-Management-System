package main.java.util;

public final class Company {

    private final String symbol;
    private final String companyName;

    /*
    private final String marketCap;
    private final String sector;
    private final String industry;
    */

    public Company(String symbol, String companyName) {
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String toString() {
        return "Company Symbol: " + symbol + ", Company Name: " + companyName;
    }
}

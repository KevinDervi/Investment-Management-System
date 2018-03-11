package main.java.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * similar to stockBuy object but quantity bought is quantity left and broker fee is not stored
 */
public class InvestmentHeld { // TODO make an equals method that compares based on transactionId
    private Long transactionId;
    private BigDecimal individualPriceBought;
    private String stockSymbol;
    private Long quantityLeft;
    private Timestamp timeBought;

    public InvestmentHeld(Long transactionId, BigDecimal individualPriceBought, String stockSymbol, Long quantityLeft, Timestamp timeBought) {
        this.transactionId = transactionId;
        this.individualPriceBought = individualPriceBought;
        this.stockSymbol = stockSymbol;
        this.quantityLeft = quantityLeft;
        this.timeBought = timeBought;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getIndividualPriceBought() {
        return individualPriceBought;
    }

    public void setIndividualPriceBought(BigDecimal individualPriceBought) {
        this.individualPriceBought = individualPriceBought;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Long getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(Long quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public Timestamp getTimeBought() {
        return timeBought;
    }

    public void setTimeBought(Timestamp timeBought) {
        this.timeBought = timeBought;
    }

    // objects are the same if their transaction id and quantity left are the same
    @Override
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof InvestmentHeld)){
            return false;
        }

        InvestmentHeld otherInvestmentHeld = (InvestmentHeld) obj;
        return transactionId.equals(otherInvestmentHeld.transactionId) && quantityLeft.equals(otherInvestmentHeld.quantityLeft);
    }
}

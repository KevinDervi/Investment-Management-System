package main.java.util;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockBought {
    private Long transactionId;
    private BigDecimal individualPrice;
    private String stockSymbol;
    private Long quantityBought;
    private BigDecimal brokerFee;
    private Timestamp timeBought;

    public StockBought(Long transactionId,BigDecimal individualPrice, String stockSymbol, Long quantityBought, BigDecimal brokerFee, Timestamp timeBought) {
        this.transactionId = transactionId;
        this.individualPrice = individualPrice;
        this.stockSymbol = stockSymbol;
        this.quantityBought = quantityBought;
        this.brokerFee = brokerFee;
        this.timeBought = timeBought;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public BigDecimal getIndividualPrice() {
        return individualPrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Long getQuantityBought() {
        return quantityBought;
    }

    public BigDecimal getBrokerFee() {
        return brokerFee;
    }

    public Timestamp getTimeBought() {
        return timeBought;
    }

    @Override
    public String toString() {
        return "Stock Bought ID: " + transactionId + ", stock Bought: " + stockSymbol + ", individual price: " + individualPrice + ", quantity bought: " + quantityBought + ", transaction fee: " + brokerFee + ", time bought: " + timeBought;
    }
}

package main.java.util;

import main.java.data.database.StockBuyDAO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class StockBought {
    private Long TransactionId;
    private String stockSymbol;
    private Long quantityBought;
    private BigDecimal brokerFee;
    private Timestamp timeBought;

    public StockBought(Long transactionId, String stockSymbol, Long quantityBought, BigDecimal brokerFee, Timestamp timeBought) {
        TransactionId = transactionId;
        this.stockSymbol = stockSymbol;
        this.quantityBought = quantityBought;
        this.brokerFee = brokerFee;
        this.timeBought = timeBought;
    }

    public Long getTransactionId() {
        return TransactionId;
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
}

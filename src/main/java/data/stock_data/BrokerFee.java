package main.java.data.stock_data;

import java.math.BigDecimal;

public class BrokerFee {
    private static final BigDecimal TRANSACTION_FEE = new BigDecimal("5");

    public static BigDecimal getTransactionFee(){
        return TRANSACTION_FEE;
    }
}

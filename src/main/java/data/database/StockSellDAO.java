package main.java.data.database;

import java.math.BigDecimal;

public class StockSellDAO {

    private StockSellDAO(){}

    public static void sellStock(Long stockBeingSoldId, BigDecimal individualStockPrice, Long quantitySold){
        // TODO check quantity told <= current investments held
    }
}

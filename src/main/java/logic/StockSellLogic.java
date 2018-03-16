package main.java.logic;

import javafx.concurrent.Task;
import main.java.data.internal_model.InvestmentsHeldByUser;
import main.java.data.stock_data.BrokerFee;

import java.math.BigDecimal;

public class StockSellLogic {

    private StockSellLogic(){}

    public static void sellStock(long id, BigDecimal individualprice, long quantitySold){
        Task buyTask = new StockSellLogic().new StockSellTask(id, individualprice, quantitySold);

        new Thread(buyTask).start();
    }

    public static BigDecimal getTotalSale(long quantity, BigDecimal individualprice){
        return individualprice.multiply(BigDecimal.valueOf(quantity));
    }

    public static BigDecimal getFinalSale(BigDecimal TotalSale) {
        return TotalSale.subtract(BrokerFee.getTransactionFee());
    }

    public static BigDecimal getProfitLoss(BigDecimal totalAmountPaid, BigDecimal finalSale){
        return finalSale.subtract(totalAmountPaid);
    }

    private class StockSellTask extends Task<Boolean> {

        private final long quantitySold;
        private final long id;
        private final BigDecimal individualprice;


        private StockSellTask(long id, BigDecimal individualprice, long quantitySold){
            this.id = id;
            this.individualprice = individualprice;
            this.quantitySold = quantitySold;
        }
        @Override
        protected Boolean call() throws Exception {
            InvestmentsHeldByUser.getInstance().sellStock(id, individualprice, quantitySold);
            return null;
        }
    }
}

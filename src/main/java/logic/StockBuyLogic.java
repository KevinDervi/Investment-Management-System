package main.java.logic;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.data.internal_model.InvestmentsHeldByUser;
import main.java.data.internal_model.UserDetails;
import main.java.data.stock_data.BrokerFee;
import main.java.util.StockData;

import java.math.BigDecimal;

public class StockBuyLogic {

    private StockBuyLogic(){}

    public static void BuyStock(long quantity){
        Task buyTask = new StockBuyLogic().new StockBuyTask(quantity);

        new Thread(buyTask).start();
    }

    public static boolean userHasEnoughFunds(long quantity){
        BigDecimal userBalance = UserDetails.getInstance().getBalance();
        BigDecimal currentStockPrice = CurrentStockInformation.getInstance().getCurrentValue();

        // (current stock cost * quantity) + transaction cost
        BigDecimal totalCost = currentStockPrice.multiply(BigDecimal.valueOf(quantity)).add(BrokerFee.getTransactionFee());

        // if userBalance is greater than the cost then return true and
        // continue with purchase
        return userBalance.compareTo(totalCost) > 0;
    }

    public static BigDecimal getTotalCost(long quantity){
        BigDecimal userBalance = UserDetails.getInstance().getBalance();
        BigDecimal currentStockPrice = CurrentStockInformation.getInstance().getCurrentValue();

        // (current stock cost * quantity)
        return currentStockPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public static BigDecimal getBrokerFee(){
        return BrokerFee.getTransactionFee();
    }

    public static BigDecimal getFinalCost(long quantity){
        // (current stock cost * quantity) + transaction cost
        return getTotalCost(quantity).add(getBrokerFee());
    }

    private class StockBuyTask extends Task<Boolean>{

        private final long quantity;

        private StockBuyTask(long quantity){
            this.quantity = quantity;
        }
        @Override
        protected Boolean call() throws Exception {
            InvestmentsHeldByUser.getInstance().buyStock(quantity);
            return null;
        }
    }
}




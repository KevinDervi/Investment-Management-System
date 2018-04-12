package main.java.data.database;

import main.java.data.stock_data.BrokerFee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockSellDAO {

    private StockSellDAO(){}

    public static void sellStock(Long stockBeingSoldId, BigDecimal individualStockPrice, Long quantitySold){

        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            Long transactionId = TransactionDAO.createTransaction();

            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String insert = "INSERT INTO StockSell " +
                    "VALUES(" +
                    transactionId + ", '" + // id of transaction
                    stockBeingSoldId + "', " + // stock symbol
                    individualStockPrice + ", " + // price of individual stock
                    quantitySold + ", " + // quantity
                    BrokerFee.getTransactionFee() + // broker fee
                    ")";

            statement.executeUpdate(insert);

            // insert what was just bought into investments held
            InvestmentsHeldDAO.sellStockFromInvestment(stockBeingSoldId, quantitySold);

            // total paid by user = (individual price * quantity) + transaction fee
            BigDecimal totalSold = individualStockPrice.multiply(BigDecimal.valueOf(quantitySold)).subtract(BrokerFee.getTransactionFee());
            System.out.println("total sold by user (amount deposited from account): " + totalSold);
            UserDAO.modifyBalanceBy(totalSold);


        } catch (SQLException e) {
            System.out.println("error with buying stock");
            e.printStackTrace();
        } finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }
}

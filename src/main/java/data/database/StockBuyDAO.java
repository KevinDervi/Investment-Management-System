package main.java.data.database;

import main.java.data.stock_data.BrokerFee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StockBuyDAO {

    private StockBuyDAO() {
    }

    public static void BuyStock(String stockSymbol, BigDecimal individualPrice, Long quantity) {
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            Long transactionId = TransactionDAO.createTransaction();

            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String insert = "INSERT INTO StockBuy " +
                    "VALUES(" +
                    transactionId + ", '" + // id of transaction
                    stockSymbol + "', " + // stock symbol
                    individualPrice + ", " + // price of individual stock
                    quantity + ", " + // quantity
                    BrokerFee.getTransactionFee() + // broker fee
                    ")";

            statement.executeUpdate(insert);

            // insert what was just bought into investments held
            InvestmentsHeldDAO.addInvesntment(transactionId, quantity);

        } catch (SQLException e) {
            System.out.println("error with buying stock");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }
}


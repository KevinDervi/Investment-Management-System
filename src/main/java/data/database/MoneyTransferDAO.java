package main.java.data.database;

import main.java.data.internal_model.UserDetails;
import main.java.util.MoneyTransferType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MoneyTransferDAO {

    private MoneyTransferDAO(){}

    public static void despoitToAccount(BigDecimal amountToAdd){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();

            Long transactionId = TransactionDAO.createTransaction();

            String insert = "INSERT INTO MoneyTransfer " +
                    "VALUES( " +
                    transactionId + " , '" + // id
                    MoneyTransferType.DEPOSIT + "', " + // transfer Type
                    amountToAdd + ")"; // amount to deposit

            statement.executeUpdate(insert);

        }catch (SQLException e){
            System.out.println("error depositing into account");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

    public static void withdrawFromAccount(BigDecimal amountToWithdraw){
        // TODO check it is not more than is currently in the account
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();

            Long transactionId = TransactionDAO.createTransaction();

            String insert = "INSERT INTO MoneyTransfer " +
                    "VALUES( " +
                    transactionId + " , '" + // id
                    MoneyTransferType.WITHDRAW + "', " + // transfer Type
                    amountToWithdraw + ")"; // amount to deposit

            statement.executeUpdate(insert);

        }catch (SQLException e){
            System.out.println("error depositing into account");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

}

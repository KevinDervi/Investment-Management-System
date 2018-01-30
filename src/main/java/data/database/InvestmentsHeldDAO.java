package main.java.data.database;

import main.java.data.account_information.UserDetails;
import main.java.data.stock_data.BrokerFee;
import main.java.util.Investment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InvestmentsHeldDAO {

    private InvestmentsHeldDAO(){}

    public static void addInvesntment(Long stockBuyId, Long quantity) {
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            Long transactionId = TransactionDAO.createTransaction();

            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String insert = "INSERT INTO InvestmentsHeld " +
                    "VALUES(" +
                    stockBuyId + ", '" + // id of transaction buy
                    UserDetails.getId() + "', " + // user id
                    quantity + ", " + // quantity bought
                    ")";

            statement.executeUpdate(insert);
        } catch (SQLException e) {
            System.out.println("error inserting invesmtment held");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

    public static void removeInvestment(Long stockBuyId){
        // TODO check that it is at 0 before removing
    }

    public static ArrayList<Investment> getAllInvestments(){
        return null;
    }
}

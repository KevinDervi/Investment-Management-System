package main.java.data.database;

import main.java.data.internal_model.UserDetails;
import main.java.util.InvestmentHeld;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvestmentsHeldDAO {

    private InvestmentsHeldDAO(){}

    public static void addInvesntment(Long stockBuyId, Long quantity) {
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String insert = "INSERT INTO InvestmentsHeld " +
                    "VALUES(" +
                    stockBuyId + ", " + // id of transaction buy
                    UserDetails.getInstance().getId() + ", " + // user id
                    quantity + // quantity bought
                    ")";
            System.out.println(insert);
            statement.executeUpdate(insert);
        } catch (SQLException e) {
            System.out.println("error inserting investment held");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

    public static Map<String, Object> getInvestment(Long stockBuyId){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        Map<String, Object> investment = null;
        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "SELECT * " +
                    "FROM InvestmentsHeld " +
                    "WHERE " +
                    "stockBuyId = " + stockBuyId;

            System.out.println(query);
            rs = statement.executeQuery(query);

            investment = DBValuesConvertToJava.convertToSingleHashMap(rs);

        } catch (SQLException e) {
            System.out.println("error inserting investment held");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        return investment;
    }

    private static void removeInvestment(Long stockBuyId){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String delete = "DELETE FROM InvestmentsHeld  " +
                    "WHERE StockBuyId = " + stockBuyId;

            System.out.println(delete);
            statement.executeUpdate(delete);
        } catch (SQLException e) {
            System.out.println("error deleting investment held");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

    public static void sellStockFromInvestment(Long investmentId, Long amountToRemove){
        // TODO check if result from removal is 0 then remove the item

        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String update = "UPDATE InvestmentsHeld" +
                    " set quantityLeft = quantityLeft - " + amountToRemove +
                    " WHERE id = '" + UserDetails.getInstance().getId() + "'";

            statement.executeUpdate(update);

            //if stock has been reduced to 0 then delete the row from InvestmentsHeld table
            /**
             * maybe have this logic in the internal model only and not in the data access
             */
//            long quantityLeft = (Long) getInvestment(investmentId).get("quantityLeft");
//
//            if (quantityLeft == 0){
//                removeInvestment(investmentId);
//                System.out.println("user has sold all of a particular stock");
//            }

        } catch (SQLException e){
            System.out.println("error with reducing amount of invesntment currently held");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

    }

    public static ArrayList<InvestmentHeld> getAllInvestments(){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        ArrayList<InvestmentHeld> investments = new ArrayList<>();
        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "SELECT transactionId, individualPrice, stockSymbol, quantityLeft, timeOfTransaction " +
                    "FROM ((InvestmentsHeld " +
                    "INNER JOIN StockBuy ON stockBuyId = transactionId )" +
                    "INNER JOIN Transaction ON transactionId = id) " +
                    "WHERE " +
                    "InvestmentsHeld.userId = " + UserDetails.getInstance().getId()
                    ;


            System.out.println(query);
            rs = statement.executeQuery(query);

            investments = convertToArrayListInvestmentHeld(rs);

        } catch (SQLException e) {
            System.out.println("error inserting investment held");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        return investments;
    }

    private static ArrayList<InvestmentHeld> convertToArrayListInvestmentHeld(ResultSet rs) throws SQLException{
        ArrayList<InvestmentHeld> data = new ArrayList<>();

        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int noOfColumns = resultSetMetaData.getColumnCount();

        while(rs.next()){

            long transactionId = rs.getLong("transactionId");
            BigDecimal individualPrice = rs.getBigDecimal("individualPrice");
            String stockSymbol = rs.getString("stockSymbol");
            long quantityLeft = rs.getLong("quantityLeft");
            Timestamp timeOfTransaction = rs.getTimestamp("timeOfTransaction");

            data.add(new InvestmentHeld(transactionId,individualPrice,stockSymbol,quantityLeft,timeOfTransaction));
        }

        return data;

    }
}

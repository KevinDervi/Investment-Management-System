package main.java.data.database;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import main.java.util.CardDetails;
import main.java.util.CardType;

import java.sql.*;

public class CardDetailsDAO {


    private CardDetailsDAO(){}

    public static void createNewCard(CardType type, String cardNumber, Date expirationDate, String nameOnCard, int securityCode) {
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query =  "INSERT INTO CardDetails " +
                    "VALUES( " +
                    "NULL , '" + // id
                    type + "',  " + // card type
                    cardNumber + " , '" + // card number
                    expirationDate + "', '" + // expiration date (must have
                    nameOnCard + "', " + // name on card
                    securityCode + ", " + // security code
                    "CURRENT_TIMESTAMP )"; // time created

            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            // gets the ID of the key just inserted
            rs = statement.getGeneratedKeys();

            rs.next(); // set pointer to the first value (which is id of the inserted row)

            Long iDOfCardJustInserted = rs.getLong(1);

            //CardUsedByDAO.attachCardToUser(iDOfCardJustInserted);

        } catch(MySQLIntegrityConstraintViolationException e){
            System.out.println("card is already in table");
            e.printStackTrace();

        } catch (SQLException e){
            System.out.println("unable to insert card into database");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

    }

    public static CardDetails getCardDetails(Long cardId){
        Connection conn = PooledDBConnection.getInstance().getConnection();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM CardDetails WHERE id = " + cardId;

            ResultSet resultSet = statement.executeQuery(query); //actual values from

            return new CardDetails(DBValuesConvertToJava.convertToSingleHashMap(resultSet));
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}

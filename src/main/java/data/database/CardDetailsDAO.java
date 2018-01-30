package main.java.data.database;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import main.java.util.CardDetails;
import main.java.util.CardType;

import java.sql.*;

public class CardDetailsDAO {


    private CardDetailsDAO(){}

    public static void createNewCard(CardType type, String cardNumber, Date expirationDate, String nameOnCard, int securityCode) {
        // TODO also add to card used by User

        Connection conn = PooledDBConnection.getInstance().getConnection();
        try {
            Statement statement = conn.createStatement();
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

            ResultSet rsOfInsertedId = statement.getGeneratedKeys();

            rsOfInsertedId.next(); // set pointer to the first value (which is id of the inserted row)

            Long iDOfCardJustInserted = rsOfInsertedId.getLong(1);
            // TODO close connection/resultSet/statement (maybe make a utility class)
            CardUsedByDAO.attachCardToUser(iDOfCardJustInserted);

        } catch(MySQLIntegrityConstraintViolationException e){
            System.out.println("card is already in table");
            e.printStackTrace();

        } catch (SQLException e){
            System.out.println("unable to insert card into database");
            e.printStackTrace();
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

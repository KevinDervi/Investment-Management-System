package main.java.data.database;

import main.java.data.internal_model.UserDetails;
import main.java.util.CardDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

public class CardUsedByDAO {

    private CardUsedByDAO(){}

    public static void attachCardToUser(Long cardToAttachId){
        Connection conn = PooledDBConnection.getInstance().getConnection();
        try {
            Statement statement = conn.createStatement();
            String query = "INSERT INTO CardUsedBy " +
                    "VALUES( " +
                    UserDetails.getInstance().getId() + ", " + // user id
                    cardToAttachId + // card id
                    " )";

            statement.executeUpdate(query);
        } catch(SQLIntegrityConstraintViolationException e){
            System.out.println("card has already been attached to user");
            e.printStackTrace();
        } catch (SQLException e){
            System.out.println("error attaching card to user");
            e.printStackTrace();
        }
    }

    public static boolean checkIfCardIsAttachedToUser(Long cardId){
        return false;
    }

    public static void RemoveCardFromUser(Long cardId){}

    public static ArrayList<CardDetails> getAllCardsAttachedToUser(){
        return null;
    }

}

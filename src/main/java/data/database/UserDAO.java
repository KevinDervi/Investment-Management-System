package main.java.data.database;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import main.java.data.internal_model.UserDetails;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Map;

public class UserDAO {

    private static String TABLE_NAME = "User";

    private UserDAO(){}

    /**
     * get all details of a single user
     * @param id primary key value of user
     * @return hash map of user information
     */
    public static Map<String, Object> getUser(int id){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "SELECT * " +
                    "FROM User " +
                    "WHERE id = " + id;

            ResultSet resultSet = statement.executeQuery(query); //actual values from query

            return DBValuesConvertToJava.convertToSingleHashMap(resultSet);

        } catch (SQLException e){
            System.out.println("error with getUser(id) query");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        //if no results/connection
        return null;

    }

    /**
     * get all details of a single user
     * @param username users username
     * @return hash map of user information
     */
    public static Map<String, Object> getUser(String username){

        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "SELECT * " +
                    "FROM User " +
                    "WHERE username = '" + username + "'";

            ResultSet resultSet = statement.executeQuery(query); //actual values from query
            // TODO close connection/resultSet/statement (maybe make a utility class)
            return DBValuesConvertToJava.convertToSingleHashMap(resultSet);

        } catch (SQLException e){
            System.out.println("error with getUser(username) query");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        //if no results/connection
        return null;

    }

    /**
     * inserts a new user into the database as long as another with conflicting details does not exist
     * @param username
     * @param password
     * @param firstname
     * @param surname
     * @param email
     */
    public static void createNewUser(String username, String password, String firstname, String surname, String email){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        conn = PooledDBConnection.getInstance().getConnection();
        try {
            statement = conn.createStatement();
            String insert = "INSERT INTO User " +
                    "VALUES( " +
                    "NULL , '" +
                    username + "',  " +
                    "PASSWORD('" + password + "')  , '" +
                    firstname + "', '" +
                    surname + "', '" +
                    email + "', " +
                    new BigDecimal(0)  + ", " +
                    "CURRENT_TIMESTAMP, " +
                    "NULL )";
            System.out.println("insert statement: "+ insert);
            System.out.println("value from insert = " + statement.executeUpdate(insert));

        } catch (MySQLIntegrityConstraintViolationException e){
            System.out.println("user already exists with that username or email");
            e.printStackTrace();
        } catch (SQLException e){
            System.out.println("error creating a new user");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

    }

    protected static boolean authenticateUser(String username, String password){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;


        try {
            conn = PooledDBConnection.getInstance().getConnection();
            statement = conn.createStatement();
            String query = "SELECT * FROM User " +
                    "WHERE " +
                    "username = '" + username + "' " +
                    "AND " +
                    "password = PASSWORD('" + password + "')";

            ResultSet resultSet = statement.executeQuery(query); //actual values from query

            if (resultSet.next()){ //if there is a user that exists with username and password match
                return true;
            }

        } catch (SQLException e){
            System.out.println("error with authenticating user");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        //if no results/connection
        return false;
    }

    public static boolean checkUsernameExists(String username) {
        return getUser(username) != null;
    }

    public static void updateCardBeingUsed(Long cardId){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            //TODO first check if the card is connected to the user via card used by table

            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "UPDATE User" +
                    " set cardBeingUsed = " + cardId +
                    " WHERE id = " + UserDetails.getId();

            statement.executeUpdate(query);

        } catch (SQLException e){
            System.out.println("error with updating CardBeingUsed table");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

    }

    /**
     * will modify the user balance by a specific value
     * @param value
     */
    public static void modifyBalanceBy(BigDecimal value){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String update = "UPDATE User" +
                    " set balance = balance + " + value +
                    " WHERE id = " + UserDetails.getId();

            statement.executeUpdate(update);

        } catch (SQLException e){
            System.out.println("error with updating user balance");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

}

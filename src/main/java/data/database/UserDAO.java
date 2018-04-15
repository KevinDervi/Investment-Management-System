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
    public static Map<String, Object> getUser(Long id){
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

            ResultSet resultSet = statement.executeQuery(query); // values from query

            return DBValuesConvertToJava.convertToSingleHashMap(resultSet); // convert resultset to a hash map

        } catch (SQLException e){
            System.out.println("error with getUser(username) query");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        //if no results/connection
        return null;
    }

    public static boolean existsEmail(String email){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "SELECT email " +
                    "FROM User " +
                    "WHERE email = '" + email+ "'";

            ResultSet resultSet = statement.executeQuery(query); //actual values from query
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        } catch (SQLException e){
            System.out.println("error with existsEmail query");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }

        return true;
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
        PreparedStatement statement = null;


        try {
            conn = PooledDBConnection.getInstance().getConnection();
            statement = conn.prepareStatement("INSERT INTO User " +
                    "VALUES( " +
                    "NULL , " +
                    "? ,  " + //username
                    "PASSWORD(?)  , " + // password
                    "? , " + // firstname
                    "? , " + // surname
                    "? , '" + // email
                    BigDecimal.ZERO  + "', " + // money (default 0)
                    "CURRENT_TIMESTAMP, " +
                    "NULL )");

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, firstname);
            statement.setString(4, surname);
            statement.setString(5, email);

            statement.executeUpdate();

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

    public static boolean authenticateUser(String username, String password) throws Exception{
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.prepareStatement("SELECT * FROM User " +
                    "WHERE " +
                    "username =  ? " +
                    "AND " +
                    "password = PASSWORD(?)");


            statement.setString(1, username);
            statement.setString(2, password);

            rs = statement.executeQuery(); // result from query

            if (rs.next()){ //if there is a user that exists with username and password match
                return true;
            }

        } catch (Exception e){
            System.out.println("error with authenticating user");
            e.printStackTrace();
            throw new Exception("cannot connect to database");
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

            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String query = "UPDATE User" +
                    " set cardBeingUsed = " + cardId +
                    " WHERE id = " + UserDetails.getInstance().getId();

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
    public static void modifyBalanceBy(BigDecimal value) throws SQLException{
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;
        System.out.println("account balance modified by: " + value);
        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String update = "UPDATE User" +
                    " set balance = balance + " + value +
                    " WHERE id = " + UserDetails.getInstance().getId();

            statement.executeUpdate(update);


        } catch (SQLException e){
            System.out.println("error with updating user balance");
            e.printStackTrace();
            throw new SQLException();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

    public static void modifyCardCurrentlyBeingUsed(Long cardId){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();
            String update = "UPDATE User" +
                    " set cardBeingUsed = " + cardId +
                    " WHERE id = " + UserDetails.getInstance().getId();

            statement.executeUpdate(update);

        } catch (SQLException e){
            System.out.println("error with updating user balance");
            e.printStackTrace();
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }

}

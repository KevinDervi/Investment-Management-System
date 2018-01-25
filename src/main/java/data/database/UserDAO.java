package main.java.data.database;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    private static String TABLE_NAME = "user";

    public static Map<String, Object> getUser(int id){

        Map<String, Object> userInfo = new HashMap<>();


        Connection conn = DBConnection.getConnection();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM User WHERE id = " + id;

            ResultSet resultSet = statement.executeQuery(query); //actual values from

            return DBValuesConvertToJava.convert(resultSet);

        } catch (SQLException e){
            System.out.println("error with query");
            e.printStackTrace();
        }

        //if no results/connection
        return null;

    }

    public static Map<String, Object> getUser(String username){

        Connection conn = DBConnection.getConnection();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM User WHERE username = '" + username + "'";

            ResultSet resultSet = statement.executeQuery(query); //actual values from query

            return DBValuesConvertToJava.convert(resultSet);

        } catch (SQLException e){
            System.out.println("error with query");
            e.printStackTrace();
        }

        //if no results/connection
        return null;

    }

    public static void createNewUser(String username, String password, String firstname, String surname, String email){
        Connection conn = DBConnection.getConnection();
        try {
            Statement statement = conn.createStatement();
            String insert = "INSERT INTO User VALUES( NULL , '" + username + "',  PASSWORD('" + password + "')  , '" + firstname + "', '" + surname + "', '" + email + "', " + new BigDecimal(0)  + ", NULL )";
            System.out.println("insert statement: "+ insert);
            System.out.println("value from insert =" + statement.executeUpdate(insert));

        } catch (SQLException e){
            System.out.println("error inserting");
            e.printStackTrace();
        }

    }

    protected static void authenticateUser(String username, String password){

    }


}

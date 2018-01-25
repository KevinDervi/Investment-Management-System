package main.java.data.database;

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

        Map<String, Object> userInfo = new HashMap<>();


        Connection conn = DBConnection.getConnection();
        try {
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM User WHERE username = " + username;

            ResultSet resultSet = statement.executeQuery(query); //actual values from query

            return DBValuesConvertToJava.convert(resultSet);

        } catch (SQLException e){
            System.out.println("error with query");
            e.printStackTrace();
        }

        //if no results/connection
        return null;

    }

    protected void insertUser(){

    }


}

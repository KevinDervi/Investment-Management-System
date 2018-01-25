package main.java.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection conn = null;
    private static String DB_ADDRESS = "localhost";
    private static String DB_PORT = "3306";
    private static String DB_TYPE = "mysql";
    private static String DB_NAME = "fyp";

    private static String DB_USERNAME = "common_user";
    private static String DB_PASSWORD = "fyp";

    private static String DRIVER_TYPE = "jdbc";
    private static String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private static String DB_URL = DRIVER_TYPE + ":" + DB_TYPE + "://" + DB_ADDRESS + ":" + DB_PORT + "/" + DB_NAME;

    private DBConnection(){}

    protected static Connection getConnection(){
        if (conn != null){
            return conn;
        }
        //if connection has not been made then create one

        try{


        Class.forName(DRIVER_NAME);

        conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);



        } catch (ClassNotFoundException e){
            System.out.println("Driver has not been found");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Connection could not be made");
            e.printStackTrace();
        }

        return conn;

    }
}

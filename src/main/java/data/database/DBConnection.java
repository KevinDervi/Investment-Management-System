package main.java.data.database;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection conn = null;
    private static final String DB_ADDRESS = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_TYPE = "mysql";
    private static final String DB_NAME = "fyp";

    private static final String DB_USERNAME = "common_user";
    private static final String DB_PASSWORD = "fyp";

    private static final String DRIVER_TYPE = "jdbc";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private static final String DB_URL = DRIVER_TYPE + ":" + DB_TYPE + "://" + DB_ADDRESS + ":" + DB_PORT + "/" + DB_NAME;

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

public class C3P0DataSource {
    private static C3P0DataSource dataSource;
    private ComboPooledDataSource comboPooledDataSource;

    private C3P0DataSource() {
        try {
            comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource
                    .setDriverClass("com.mysql.jdbc.Driver");
            comboPooledDataSource
                    .setJdbcUrl("jdbc:mysql://localhost:3306/testdb");
            comboPooledDataSource.setUser("root");
            comboPooledDataSource.setPassword("secret");
        }
      catch (PropertyVetoException ex1) {
                ex1.printStackTrace();
            }
        }

        public static C3P0DataSource getInstance() {
            if (dataSource == null)
                dataSource = new C3P0DataSource();
            return dataSource;
        }

        public Connection getConnection() {
            Connection con = null;
            try {
                con = comboPooledDataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return con;
        }
    }
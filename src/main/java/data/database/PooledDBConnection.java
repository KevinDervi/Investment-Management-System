package main.java.data.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class PooledDBConnection {

    private static final String DB_ADDRESS = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_TYPE = "mysql";
    private static final String DB_NAME = "fyp";

    private static final String DB_USERNAME = "common_user";
    private static final String DB_PASSWORD = "fyp";

    private static final String DRIVER_TYPE = "jdbc";
    private static final String DRIVER_NAME = "com.mysql.jdbc.Driver";

    private static final String DB_URL = DRIVER_TYPE + ":" + DB_TYPE + "://" + DB_ADDRESS + ":" + DB_PORT + "/" + DB_NAME;

    private static PooledDBConnection C3PODBConnection;
    private ComboPooledDataSource comboPooledDBConnection;

    /**
     * singleton class
     */
    private PooledDBConnection() {
        try {
            comboPooledDBConnection = new ComboPooledDataSource();

            comboPooledDBConnection.setDriverClass(DRIVER_NAME);
            comboPooledDBConnection.setJdbcUrl(DB_URL);

            comboPooledDBConnection.setUser(DB_USERNAME);
            comboPooledDBConnection.setPassword(DB_PASSWORD);
        }
        catch (PropertyVetoException ex1) {
            System.out.println("error setting up database connection");
            ex1.printStackTrace();
        }
    }

    /**
     *
     * @return instance of the pooled connection
     */
    public static PooledDBConnection getInstance() {
        if (C3PODBConnection == null)
            C3PODBConnection = new PooledDBConnection();
        return C3PODBConnection;
    }

    /**
     * takes an unused connection from the pool and returns it
     * @return
     */
    public Connection getConnection() throws SQLException {
            return comboPooledDBConnection.getConnection();
    }

}
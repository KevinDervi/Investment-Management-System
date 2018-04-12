package main.java.data.database;

import main.java.data.internal_model.UserDetails;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionDAO {

    private TransactionDAO(){}

    /**
     * methods are all protected because it should not be created on its own, rather only created when a transaction type is called
     */
    protected static Long createTransaction(){
        Connection conn = null;
        ResultSet rs = null;
        Statement statement = null;

        try {
            conn = PooledDBConnection.getInstance().getConnection();

            statement = conn.createStatement();

            String insert = "INSERT INTO Transaction " +
                    "VALUES( " +
                    "NULL , '" + // id
                    UserDetails.getInstance().getId() + "', " + // user id
                    "CURRENT_TIMESTAMP)"; // time of transaction

            statement.executeUpdate(insert, Statement.RETURN_GENERATED_KEYS);

            ResultSet rsOfInsertedId = statement.getGeneratedKeys();

            rsOfInsertedId.next(); // set pointer to the first value (which is id of the inserted row)

            Long idOfTransactionJustInserted = rsOfInsertedId.getLong(1);

            return idOfTransactionJustInserted;

        } catch (SQLException e){
            System.out.println("error creating transaction");
            e.printStackTrace();
            return -1L; // return -1 to cause an error in inserting to the database
        }finally {
            PooledDBConnection.getInstance().closeConnection(conn, statement, rs);
        }
    }


}

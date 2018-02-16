package main.java.logic;

import main.java.data.internal_model.UserDetails;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * handles all logic relating to user details
 */
public class UserDetailsLogic {

    public static String getUsername(){
        return UserDetails.getInstance().getUsername();
    }

    public static BigDecimal getBalalance(){
        return UserDetails.getInstance().getBalance();
    }

    public static void updateBalance(BigDecimal amount) throws SQLException {
        UserDetails.getInstance().updateBalance(amount);
    }
}

package main.java.logic;

import javafx.beans.property.SimpleObjectProperty;
import main.java.data.internal_model.UserDetails;

import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * handles all logic relating to user details
 */
public class UserDetailsLogic {

    public static String getUsername(){ return UserDetails.getInstance().getUsername();}

    public static BigDecimal getBalance(){ return UserDetails.getInstance().getBalance();}

    public static SimpleObjectProperty<BigDecimal> getBalanceProperty(){ return UserDetails.getInstance().balanceProperty();}

    public static void updateBalance(BigDecimal amount) throws SQLException {
        UserDetails.getInstance().updateBalance(amount);
    }

    public static void deposit(BigDecimal amount){
        UserDetails.getInstance().desposit(amount);
    }

    public static void withdraw(BigDecimal amount){
        UserDetails.getInstance().withdraw(amount);
    }}

package main.java.logic;

import main.java.data.database.UserDAO;

/**
 * performs the necessary checks before adding a user to the database
 */
public class SignUpLogic {

    private SignUpLogic(){}


    public static boolean createUser(String username, String password, String firstName, String surname, String email){
        UserDAO.createNewUser(username, password, firstName, surname, email);
        return true;
    }




}

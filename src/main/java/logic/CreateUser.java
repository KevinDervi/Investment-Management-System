package main.java.logic;

import main.java.data.database.UserDAO;

/**
 * performs the necessary checks before adding a user to the database
 */
public class CreateUser {
    private static int MAX_TEXT_LENGTH = 40;
    private static int MIN_TEXT_LENGTH = 3;

    private static int MAX_USERNAME_LENGTH = 16;
    private static int MIN_USERNAME_LENGTH = 5;

    private static int MAX_PASSWORD_LENGTH = 16;
    private static int MIN_PASSWORD45_LENGTH = 5;

    private CreateUser(){}

    /**
     * checks if the username follows guidelines and has not been taken by another user
     * @param username
     * @return
     */
    public static boolean usernameIsValid (String username){
        boolean isValid = true;

        if(UserDAO.checkUsernameExists(username)){
            isValid = false;
        }

        if(username.length() > MAX_USERNAME_LENGTH){
            isValid = false;
        }

        if(username.length() < MIN_USERNAME_LENGTH){
            isValid = false;
        }

        if(!isAlphaneumeric(username)){
            isValid = false;
        }

        return isValid;
    }

    public static boolean textIsValid(String text){
        if (text.length() <= MAX_TEXT_LENGTH){
            return true;
        }
        return false;
    }

    public static boolean emailIsValid(String email){
        // example match steve+work@hotmail.com
        return email.matches("^[a-zA-Z0-9+]+@[a-zA-Z]+\\.?[a-zA-Z]+\\.[a-zA-Z]+$");
    }

    public static boolean createUser(String username, String password, String firstName, String surname, String email){
        UserDAO.createNewUser(username, password, firstName, surname, email);
        return true;
    }

    // utility functions
    private static boolean isAlphaneumeric(String text){
        return text.matches("[a-zA-Z0-9]*");
    }

    private static boolean isAlphabetOnly(String text){
        return text.matches("[a-zA-Z]*");
    }


}

package main.java.logic;

import main.java.data.database.UserDAO;

public class validateInput {
    private static final int MAX_TEXT_LENGTH = 40;
    private static final int MIN_TEXT_LENGTH = 2;

    private static final int MAX_USERNAME_LENGTH = 16;
    private static final int MIN_USERNAME_LENGTH = 5;

    private static final int MAX_PASSWORD_LENGTH = 16;
    private static final int MIN_PASSWORD45_LENGTH = 5;

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

    // utility functions
    private static boolean isAlphaneumeric(String text){
        return text.matches("[a-zA-Z0-9]*");
    }

    private static boolean isAlphabetOnly(String text){
        return text.matches("[a-zA-Z]*");
    }
}

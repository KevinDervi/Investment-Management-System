package main.java.logic;

import main.java.data.database.UserDAO;

public class validateInput {
    private static final int MAX_TEXT_LENGTH = 40;
    private static final int MIN_TEXT_LENGTH = 2;

    private static final int MAX_USERNAME_LENGTH = 16;
    private static final int MIN_USERNAME_LENGTH = 5;

    private static final int MAX_PASSWORD_LENGTH = 16;
    private static final int MIN_PASSWORD_LENGTH = 5;

    /**
     * username guideline functions below
     */
    public static boolean isUsernameTaken (String username){
        return UserDAO.checkUsernameExists(username);
    }

    public static boolean isUsernameTooLong(String username){
        return username.length() > MAX_USERNAME_LENGTH;
    }

    public static boolean isUsernameTooShort (String username){
        return username.length() < MIN_USERNAME_LENGTH;
    }

    /**
     * password guideline functions below
     */

    public static boolean isPasswordTooLong(String password){
        return password.length() > MAX_PASSWORD_LENGTH;
    }

    public static boolean isPasswordTooShort(String password){
        return password.length() < MIN_PASSWORD_LENGTH;
    }

    public static boolean isEmailValidFormat(String email){
        // example match steve+work@hotmail.com
        return email.matches("^[a-zA-Z0-9+]+@[a-zA-Z]+\\.?[a-zA-Z]+\\.[a-zA-Z]+$");
    }

    public static boolean isEmailTaken(String email){
        return UserDAO.existsEmail(email);
    }

    // generic guideline functions

    public static boolean isTooLong(String text){
        return text.length() >= MAX_TEXT_LENGTH;
    }

    public static boolean isTooShort(String text){
        return text.length() < MIN_TEXT_LENGTH;
    }

    public static boolean isAlphaneumeric(String text){
        return text.matches("[a-zA-Z0-9]*");
    }

    public static boolean isAlphabetOnly(String text){
        return text.matches("[a-zA-Z]*");
    }
}

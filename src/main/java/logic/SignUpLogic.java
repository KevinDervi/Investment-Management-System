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

    /**
     * all checks return null if no error message is found
     */
    public static String getUsernameErrorMessage(String username){

        if(validateInput.isUsernameTooShort(username)){
            return "Username is too short";
        }

        if(validateInput.isUsernameTooLong(username)){
            return "Username is too long";
        }

        if(!validateInput.isAlphaneumeric(username)){
            return "Username should be Alphanumeric";
        }

        // username checks should be last as it uses database connection
        if(validateInput.isUsernameTaken(username)){
            return "Username has been taken";
        }

        // else return null if no error message returned
        return null;
    }

    public static String getGenericTextErrorMessage(String text){
        if(validateInput.isTooLong(text)){
            return "Text is too long";
        }

        if(validateInput.isTooShort(text)){
            return "Text is too short";
        }

        if(!validateInput.isAlphabetOnly(text)){
            return "Text is not alphabet only";
        }

        return null;
    }

    public static String getEmailErrorMessage(String email){
        if (!validateInput.isEmailValidFormat(email)){
            return "Not valid E-mail format";
        }

        if(validateInput.isEmailTaken(email)){
            return "E-mail is already used";
        }

        return null;
    }

    public static String getPasswordErrorMessage(String password){
        if(validateInput.isPasswordTooLong(password)){
            return "Password is too long";
        }

        if (validateInput.isPasswordTooShort(password)){
            return "Password is too short";
        }

        return null;
    }


    public static String getReTypedPasswordMessage(String password, String reTypedPassword){

        if(!password.equals(reTypedPassword)){
            return "Does not match password";
        }

        return null;
    }

}

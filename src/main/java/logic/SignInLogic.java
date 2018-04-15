package main.java.logic;

import main.java.data.internal_model.UserDetails;

public class SignInLogic {

    private SignInLogic() {
    }


    public static boolean Authentication(String username, String password) throws Exception{
        return UserDetails.getInstance().authenticateUser(username,password );
    }

    public static void loadUser(String username){
        UserDetails.getInstance().initialiseUserDetails(username);
    }


}

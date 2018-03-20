package main.java.logic;

import main.java.data.internal_model.CurrentStockInformation;
import main.java.data.internal_model.InvestmentsHeldByUser;
import main.java.data.internal_model.UserDetails;

public class SignOutLogic {

    private SignOutLogic(){}

    public static void signOut(){

        // destroy the internal model that contains all the user data foricing the new user to create a new internal model
        // so no data is accidentally transferred

        InvestmentsHeldByUser.getInstance().signOut();

        UserDetails.getInstance().signOut();

        CurrentStockInformation.getInstance().signOut();
    }
}

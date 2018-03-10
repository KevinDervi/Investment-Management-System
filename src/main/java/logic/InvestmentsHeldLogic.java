package main.java.logic;

import javafx.collections.ListChangeListener;
import main.java.data.internal_model.InvestmentsHeldByUser;

import java.math.BigDecimal;

public class InvestmentsHeldLogic {


    public static void addListenerToInvestmentsHeld(ListChangeListener listener){
        InvestmentsHeldByUser.getInstance().addListenerToInvestmentsHeld(listener);
    }

    public static void updateInvestmentsHeld(){
        InvestmentsHeldByUser.getInstance().updateInvestmentsHeld();
    }

    public static BigDecimal getCurrentPrice(String symbol){
        return InvestmentsHeldByUser.getInstance().getInvestmentsHeldCurrentValues().get(symbol);
    }
}

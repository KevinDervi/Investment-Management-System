package main.java.logic;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import main.java.data.internal_model.InvestmentsHeldByUser;
import main.java.util.InvestmentHeld;

import java.math.BigDecimal;

public class InvestmentsHeldLogic {


    public static void addListenerToInvestmentsHeld(ListChangeListener listener){
        InvestmentsHeldByUser.getInstance().addListenerToInvestmentsHeld(listener);
    }

    public static SimpleListProperty<InvestmentHeld> getObservableInvestmentHeldList(){
        return InvestmentsHeldByUser.getInstance().getInvestmentsHeld();
    }

    public static void updateInvestmentsHeld(){
        InvestmentsHeldByUser.getInstance().updateInvestmentsHeld();
    }

    public static BigDecimal getCurrentPrice(String symbol){
        return InvestmentsHeldByUser.getInstance().getInvestmentsHeldCurrentValues().get(symbol);
    }
}

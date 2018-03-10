package main.java.data.internal_model;


import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import main.java.data.database.InvestmentsHeldDAO;
import main.java.data.database.StockBuyDAO;
import main.java.data.database.StockSellDAO;
import main.java.data.stock_data.StockDataAPI;
import main.java.util.InvestmentHeld;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class InvestmentsHeldByUser {
    // TODO holds all information regarding the values held by the user
    
    private ObservableList<InvestmentHeld> InvestmentsHeld = FXCollections.observableArrayList();

    private HashMap<String, BigDecimal> InvestmentsHeldCurrentValues = new HashMap<>();
    
    private static InvestmentsHeldByUser instance;

    private InvestmentsHeldByUser() {
        updateInvestmentsHeld();
    }

    public static InvestmentsHeldByUser getInstance() {
        if(instance == null){
            instance = new InvestmentsHeldByUser();
        }
        
        return instance;
    }

    public HashMap<String, BigDecimal> getInvestmentsHeldCurrentValues() {
        return InvestmentsHeldCurrentValues;
    }

    public void updateInvestmentsHeld(){
        updateInvestmentsHeldCurrentvalues();

        List<InvestmentHeld> userInvestments = InvestmentsHeldDAO.getAllInvestments();

        if (userInvestments!= null){
            // TODO add and remove values based of difference do not just use set all as it will deselect the currently selected listview
            InvestmentsHeld.setAll(userInvestments);
        }
    }

    private void updateInvestmentsHeldCurrentvalues() {
        HashSet<String> setOfSymbols = new HashSet<>();

        List<InvestmentHeld> userInvestments = InvestmentsHeldDAO.getAllInvestments();

        // if user has no current investments then do nothing
        if (userInvestments == null) return;


        for (InvestmentHeld i: userInvestments) {
            setOfSymbols.add(i.getStockSymbol());
        }

        try {
        InvestmentsHeldCurrentValues = toInvestmentsHeldValuesHashMap(StockDataAPI.getmultipleLatestStockData(setOfSymbols));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private HashMap<String, BigDecimal> toInvestmentsHeldValuesHashMap(JSONArray jsonObjects) throws Exception{
        HashMap<String, BigDecimal> hashMap = new HashMap<>();

        if(jsonObjects == null){
            return hashMap;
        }

        for (int i = 0; i < jsonObjects.length(); i++) {

            JSONObject jsonObject = jsonObjects.getJSONObject(i);

            // get price as string instead of double to avoid losing information
            String value = jsonObject.getString("2. price");

            String symbol = jsonObject.getString("1. symbol");

            //update our current value
            hashMap.put(symbol, new BigDecimal(value));
        }

        return hashMap;
    }

    public void addListenerToInvestmentsHeld(ListChangeListener listener){
        InvestmentsHeld.addListener(listener);
    }

    public void buyStock(String symbol, BigDecimal individualPrice, long quantity){
        StockBuyDAO.BuyStock(symbol, individualPrice, quantity);
        updateInvestmentsHeld();
    }

    public void sellStock(long id, BigDecimal individualPrice, long quantitySold){
        StockSellDAO.sellStock(id, individualPrice, quantitySold);
        updateInvestmentsHeld();
    }

}

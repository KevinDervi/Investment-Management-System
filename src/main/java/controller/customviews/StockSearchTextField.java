package main.java.controller.customviews;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import main.java.logic.StockDataLogic;
import main.java.util.Company;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StockSearchTextField extends TextField {
    private final ContextMenu contextMenuStocks = new ContextMenu();
    private HashSet<StockSearchMenuItem> stockSearchMenuItemSet = new HashSet<>();
    private ObservableSet<StockSearchMenuItem> stockSearchMenuItems = FXCollections.observableSet();
    private ObservableSet<StockSearchMenuItem> filteredStockSearchMenuItems;

    private static final int CONTEXT_MENU_DISPLAY_LIMIT = 20;

    public StockSearchTextField() {
        super();

        initialiseCompanyList();

        this.setContextMenu(contextMenuStocks);

        // filter context menu when typing
        textProperty().addListener(this::onTextChanged);

    }

    private void initialiseCompanyList() {
        HashSet<StockSearchMenuItem> set = new HashSet<>();

        for (Company c: StockDataLogic.getSetOfCompanies()) {
            set.add(new StockSearchMenuItem(c));
        }

        stockSearchMenuItemSet = set;
        stockSearchMenuItems.addAll(set);
        //contextMenuStocks.getItems().addAll(stockSearchMenuItems);
    }

    private void onTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            contextMenuStocks.hide();
            System.out.println("context menu hidden ==================================");
        } else {

            // deep copy required otherwise original set is passed by reference
            Set<StockSearchMenuItem> deepCopy = new HashSet<>(stockSearchMenuItemSet);
            filteredStockSearchMenuItems = FXCollections.observableSet(deepCopy);

            // filter list
            filteredStockSearchMenuItems.removeIf(stockSearchMenuItem -> {
                String valueEntered = newValue.toLowerCase();
                String menuItemSymbol = stockSearchMenuItem.getSymbol().toLowerCase();
                String menuItemCompanyName = stockSearchMenuItem.getCompanyName().toLowerCase();

                // if String entered by user is not contained in the symbol or company name then remove
                return !menuItemSymbol.contains(valueEntered) && !menuItemCompanyName.contains(valueEntered);
                // else keep in list
            });

            // limit size to display
            if(filteredStockSearchMenuItems.size() > CONTEXT_MENU_DISPLAY_LIMIT){
                ObservableSet<StockSearchMenuItem> temp = FXCollections.observableSet();
                Iterator iterator = filteredStockSearchMenuItems.iterator();
                for (int i = 0; i < CONTEXT_MENU_DISPLAY_LIMIT; i++) {
                    temp.add((StockSearchMenuItem) iterator.next());
                }

                filteredStockSearchMenuItems.clear();
                filteredStockSearchMenuItems.addAll(temp);
            }
            contextMenuStocks.getItems().clear();
            contextMenuStocks.getItems().setAll(filteredStockSearchMenuItems);
            contextMenuStocks.show(this, Side.BOTTOM, 0, 0);
            System.out.println("context menu shown =========================== " + filteredStockSearchMenuItems.size());

        }
    }


    private class StockSearchMenuItem extends MenuItem{

        @FXML
        private HBox menuItemContainer;

        @FXML
        private Label symbol;

        @FXML
        private Label companyName;

        StockSearchMenuItem(Company company) {
            super();


            // load fxml when creating cell
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/StockSearchContextMenuItem.fxml"));
            fxmlLoader.setController(this);

            try {
                fxmlLoader.load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("unable to load FXMl when creating a Stock Search Menu Item");
            }

            symbol.setText(company.getSymbol());
            companyName.setText(company.getCompanyName());

            setText(null);
            setGraphic(menuItemContainer);
        }

        public String getSymbol() {
            return symbol.getText();
        }

        public String getCompanyName() {
            return companyName.getText();
        }
    }
}

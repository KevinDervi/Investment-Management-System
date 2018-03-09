package main.java.controller.customviews;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import main.java.logic.StockDataLogic;
import main.java.util.Company;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import javafx.scene.control.Label;

public class StockSearchField extends ComboBox<Company> {
    private HashSet<Company> stockSearchMenuItemSet = new HashSet<>();
    private ObservableSet<Company> stockSearchCells = FXCollections.observableSet();
    private ObservableSet<Company> filteredStockSearchCells;

    private static final int CONTEXT_MENU_DISPLAY_LIMIT = 20;

    // TODO make search text field a combo box instead since context menu cannot get selected item
    public StockSearchField() {
        super();

        initialiseCompanyList();

        // allow the user to type text
        setEditable(true);

        // filter context menu when typing
        this.getEditor().textProperty().addListener(this::onTextChanged);


        this.setCellFactory(param -> new CompanyCell());


    }

    private void initialiseCompanyList() {
//        HashSet<Company> set = new HashSet<>();
//
//        for (Company c : StockDataLogic.getSetOfCompanies()) {
//            set.add(new CompanyCell(c));
//        }
//      // TODO call Stock data logic only once
        stockSearchMenuItemSet = new HashSet<>(StockDataLogic.getSetOfCompanies());

        // add set of companies for filtering and displaying
        stockSearchCells.addAll(StockDataLogic.getSetOfCompanies());
    }

    private void onTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue.length() == 0) {
            System.out.println("context menu hidden ==================================");
        } else {

            // deep copy required otherwise original set is passed by reference
            Set<Company> deepCopy = new HashSet<>(stockSearchMenuItemSet);
            filteredStockSearchCells = FXCollections.observableSet(deepCopy);

            // filter list
            filteredStockSearchCells.removeIf(stockSearchCompany -> {
                String valueEntered = newValue.toLowerCase();
                String menuItemSymbol = stockSearchCompany.getSymbol().toLowerCase();
                String menuItemCompanyName = stockSearchCompany.getCompanyName().toLowerCase();

                // if String entered by user is not contained in the symbol or company name then remove
                return !menuItemSymbol.contains(valueEntered) && !menuItemCompanyName.contains(valueEntered);
                // else keep in list
            });

            // limit size to display
            if (filteredStockSearchCells.size() > CONTEXT_MENU_DISPLAY_LIMIT) {
                ObservableSet<Company> temp = FXCollections.observableSet();
                Iterator iterator = filteredStockSearchCells.iterator();
                for (int i = 0; i < CONTEXT_MENU_DISPLAY_LIMIT; i++) {
                    temp.add((Company) iterator.next());
                }

                // clear any previous values
                filteredStockSearchCells.clear();
                filteredStockSearchCells.addAll(temp);
            }
            this.getItems().clear();
            this.getItems().setAll(filteredStockSearchCells);

            System.out.println("context menu shown =========================== " + filteredStockSearchCells.size());

        }
    }


    public class CompanyCell extends ListCell<Company> {

        @FXML
        private HBox menuItemContainer;

        @FXML
        private Label symbol;

        @FXML
        private Label companyName;

        CompanyCell() {
            super();


            // load fxml when creating cell
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/StockSearchCompanyListCell.fxml"));
            fxmlLoader.setController(this);

            try {
                fxmlLoader.load();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("unable to load FXMl when creating a Stock Search Menu Item");
            }

//            symbol.setText(company.getSymbol());
//            companyName.setText(company.getCompanyName());

//            setText(null);
//            setGraphic(menuItemContainer);
        }

        @Override
        protected void updateItem(Company company, boolean empty) {

            super.updateItem(company, empty);

            System.out.println("update stock search item");
            // standard required for the cell
            if(empty){
                setGraphic(null);
                setText(null);
            }else{
                // update the cell here

                symbol.setText(company.getSymbol());
                companyName.setText(company.getCompanyName());

                // finally set graphic as grid

                setGraphic(menuItemContainer);
                setText(null);
            }
        }

        public String getSymbol() {
            return symbol.getText();
        }

        public String getCompanyName() {
            return companyName.getText();
        }
    }
}

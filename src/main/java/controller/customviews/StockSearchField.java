package main.java.controller.customviews;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import main.java.logic.StockDataLogic;
import main.java.util.Company;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import javafx.scene.control.Label;

public class StockSearchField extends ComboBox<Company> {
    private HashSet<Company> stockSearchMenuItemSet = new HashSet<>();
    ObservableSet<Company> filteredStockSearchCells = FXCollections.observableSet();

    private static final int LIST_DISPLAY_LIMIT = 20;

    // TODO make search text field a combo box instead since context menu cannot get selected item
    public StockSearchField() {
        super();

        initialiseCompanyList();

        // allow the user to type text
        setEditable(true);

        setPromptText("Search For Stock");

        // filter context menu when typing
        this.getEditor().textProperty().addListener(this::onTextChanged);

        this.setConverter(new StringConverter<>() {
            @Override
            public String toString(Company object) {
                if (object == null) return null;
                return null;//object.getSymbol();
            }

            @Override
            public Company fromString(String string) {
                return null;
            }
        });

        this.setCellFactory(param -> new CompanyCell());

        initialiseCSS();

    }

    private void initialiseCSS() {
        AnchorPane.setRightAnchor(this, 0.0);
        AnchorPane.setLeftAnchor(this, 0.0);

        this.getStyleClass().clear();
        this.getStyleClass().add("search-combo-box");

    }

    private void initialiseCompanyList() {
        // store whole list of companies and
        stockSearchMenuItemSet = new HashSet<>(StockDataLogic.getSetOfCompanies());
    }

    private void onTextChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // hide before showing to graphically update dropdown menu
        this.hide();


        // if there is a non null value that is greater than 0 then update dropdown menu
        if (newValue != null && newValue.length() > 0) {
            this.getItems().clear();
            // deep copy required otherwise original set is passed by reference
            Set<Company> deepCopy = new HashSet<>(stockSearchMenuItemSet);

            // observable set that will be filtered for display
            filteredStockSearchCells.clear();
            filteredStockSearchCells.addAll(deepCopy);

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
            if (filteredStockSearchCells.size() > LIST_DISPLAY_LIMIT) {
                ObservableSet<Company> temp = FXCollections.observableSet();
                Iterator iterator = filteredStockSearchCells.iterator();
                for (int i = 0; i < LIST_DISPLAY_LIMIT; i++) {
                    temp.add((Company) iterator.next());
                }

                // clear any previous values
                filteredStockSearchCells.clear();
                filteredStockSearchCells.addAll(temp);
            }



            // if there is at least 1 result then show dropdown otherwise keep it hidden
            if(filteredStockSearchCells.size() > 0){
                this.getItems().addAll(filteredStockSearchCells);
                this.show();
            }
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

        }

        @Override
        protected void updateItem(Company company, boolean empty) {

            super.updateItem(company, empty);

            // standard required for the cell
            if(empty || company == null){
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

        @Override
        public String toString() {
            return "Company with Symbol: " + getSymbol();
        }
    }
}

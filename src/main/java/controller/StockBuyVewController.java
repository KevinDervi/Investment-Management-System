package main.java.controller;


import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.data.internal_model.CurrentStockInformation;
import main.java.logic.StockBuyLogic;
import main.java.logic.StockDataLogic;

import java.math.BigDecimal;

public class StockBuyVewController {

    @FXML
    private Label labelErrorMessage;

    @FXML
    private Label labelStockSymbol;

    @FXML
    private Label labelCurrentPrice;

    @FXML
    private Label labelTotalCostPreCommision;

    @FXML
    private Button buttonPurchase;

    @FXML
    private Button buttonCancel;

    @FXML
    private TextField textFieldQuantity;

    @FXML
    private Label labelComission;

    @FXML
    private Label labelFinalCost;

    private boolean validQuantity = false;
    private Long quantity;

    private InvestmentManagementViewController mainWindowController;

    @FXML
    public void initialize() {
        labelStockSymbol.setText(CurrentStockInformation.getInstance().getStockSymbol());

        // update current price
        StockDataLogic.getCurrentValueProperty().addListener(this::onCurrentValueChanged);

        textFieldQuantity.textProperty().addListener(this::onQuantityChanged);

    }

    public void setMainWindowController(InvestmentManagementViewController mainWindowController){
        this.mainWindowController = mainWindowController;
    }

    private void quantityIsValid(long quantity) {
        if (StockBuyLogic.userHasEnoughFunds(quantity)) {
            this.quantity = quantity;
            validQuantity = true;
            updateCosts();
        } else {
            resetCosts();
            this.quantity = null;
            validQuantity = false;

            labelErrorMessage.setText("Not enough funds to purchase that amount");
            labelErrorMessage.setVisible(true);
        }
    }

    private void resetCosts() {
        labelTotalCostPreCommision.setText("$0");
        labelFinalCost.setText("$0");
    }

    private void updateCosts() {
         labelTotalCostPreCommision.setText("$" + StockBuyLogic.getTotalCost(quantity).toString());
         labelComission.setText("$" + StockBuyLogic.getBrokerFee().toString());
         labelFinalCost.setText("$" + StockBuyLogic.getFinalCost(quantity).toString());
    }

    private void onCurrentValueChanged(ObservableValue<? extends BigDecimal> observable, BigDecimal oldValue, BigDecimal newValue) {
        labelCurrentPrice.setText("$" + newValue.toString());
        if (this.quantity != null){
            quantityIsValid(this.quantity);
        }

    }

    private void onQuantityChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        // if non 0 positive integer
        if (newValue.matches("[0-9]+") || !newValue.equals("0")) {
            labelErrorMessage.setVisible(false);

            long quantity = Long.valueOf(newValue);

            quantityIsValid(quantity);

        } else {
            labelErrorMessage.setText("Please enter a valid amount");
            labelErrorMessage.setVisible(true);
        }
    }

    @FXML
    void handleCancelButton(ActionEvent event){
        mainWindowController.removePopUp();
    }

    @FXML
    void handlePurchaseButton(ActionEvent event){
        if (validQuantity){
            StockBuyLogic.BuyStock(quantity);
        }
    }
}

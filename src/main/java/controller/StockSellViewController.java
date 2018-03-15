package main.java.controller;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.logic.StockBuyLogic;
import main.java.logic.StockDataLogic;
import main.java.logic.StockSellLogic;
import main.java.util.InvestmentHeld;

import java.math.BigDecimal;

public class StockSellViewController {
    @FXML
    private Label labelStockSymbol;

    @FXML
    private Label labelErrorMessage;

    @FXML
    private Label labelCurrentPrice;

    @FXML
    private Label labelQuantityOwned;

    @FXML
    private TextField textFieldQuantityToSell;

    @FXML
    private Label labelTotalSalePreComission;

    @FXML
    private Label labelFinalSale;

    @FXML
    private Label labelProfitLoss;

    private InvestmentHeld investmentSelected;
    private InvestmentManagementViewController mainController;
    private boolean quantityIsValid;
    private Long quantity;

    @FXML
    public void initialize(){


        initialiseTextFieldQuantity();

        intialiseCurrentprice();

        initialiseSaleLabels();
    }

    private void initialiseSaleLabels() {
        // add placeholder at index 1 so style class can be modified later
        labelProfitLoss.getStyleClass().add("empty");
        resetSales();
    }

    private void intialiseCurrentprice() {
        labelCurrentPrice.textProperty().bind(StockDataLogic.getCurrentValueProperty().asString());
        labelCurrentPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSales();
        });
    }

    private void initialiseTextFieldQuantity() {
        textFieldQuantityToSell.textProperty().addListener(this::onQuantityChanged);
    }

    private void onQuantityChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue == null){
            resetSales();
            return;
        }

        // if non 0 positive integer
        if (newValue.matches("[0-9]+") && !newValue.equals("0")) {
            labelErrorMessage.setVisible(false);

            long quantity = Long.valueOf(newValue);

            quantityIsValid(quantity);

        } else {
            resetSales();
            labelErrorMessage.setText("Choose between 1 and " + investmentSelected.getQuantityLeft().toString() + " stocks to sell");
            labelErrorMessage.setVisible(true);
        }
    }

    private void resetSales() {
        labelFinalSale.setText("$0");
        labelProfitLoss.setText("$0");
        labelTotalSalePreComission.setText("$0");
    }

    private void quantityIsValid(long quantity) {
        if (quantity <= investmentSelected.getQuantityLeft()) {
            this.quantity = quantity;
            quantityIsValid = true;
            updateSales();
        } else {
            resetSales();
            this.quantity = null;
            quantityIsValid = false;

            labelErrorMessage.setText("Choose between 1 and " + investmentSelected.getQuantityLeft().toString() + " stocks to sell");
            labelErrorMessage.setVisible(true);
        }
    }

    private void updateSales() {
        BigDecimal totalSale = StockSellLogic.getTotalSale(quantity, StockDataLogic.getCurrentValueProperty().getValue());
        BigDecimal finalSale= StockSellLogic.getFinalSale(totalSale);

        BigDecimal totalAmountPaid = investmentSelected.getIndividualPriceBought().multiply(BigDecimal.valueOf(quantity));
        BigDecimal profitLoss = StockSellLogic.getProfitLoss(totalAmountPaid, finalSale);


        labelTotalSalePreComission.setText("$" + totalSale.toString());
        labelFinalSale.setText("$" + finalSale.toString());

        // colour the profit loss green or red based on actual profit
        if(profitLoss.compareTo(BigDecimal.ZERO) > 0){
            labelProfitLoss.getStyleClass().set(1, "label-colour-green");
            labelProfitLoss.setText("+$" + profitLoss.toString());
        }else {
            labelProfitLoss.getStyleClass().set(1, "label-colour-red");
            labelProfitLoss.setText("-$" + profitLoss.toString());
        }

    }

    @FXML
    void handleCancelButton(ActionEvent event) {
        mainController.removePopUp();
    }

    @FXML
    void handleSellButton(ActionEvent event) {
        if (quantityIsValid){
            StockSellLogic.sellStock(investmentSelected.getTransactionId(), StockDataLogic.getCurrentValueProperty().getValue(), quantity);
            mainController.removePopUp();
        }
    }

    public void setMainController(InvestmentManagementViewController mainController) {
        this.mainController = mainController;
    }

    public void setInvestmentSelected(InvestmentHeld investmentSelected){
        this.investmentSelected = investmentSelected;

        // initialise variables here
        labelStockSymbol.setText(investmentSelected.getStockSymbol());

        labelQuantityOwned.setText(investmentSelected.getQuantityLeft().toString());
    }
}

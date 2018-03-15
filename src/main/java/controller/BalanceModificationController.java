package main.java.controller;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.logic.UserDetailsLogic;

import java.math.BigDecimal;

public class BalanceModificationController {

    @FXML
    private Label labelErrorMessage;

    @FXML
    private ComboBox<String> comboBoxType;

    @FXML
    private TextField textFieldAmount;

    private InvestmentManagementViewController mainController;
    private boolean validAmount = false;

    @FXML
    public void initialize(){

        iniialiseComboBoxType();

        initialiseTextFieldAmount();
    }

    private void initialiseTextFieldAmount() {
        textFieldAmount.textProperty().addListener(this::onAmountChanged);
    }

    private void iniialiseComboBoxType() {
        comboBoxType.getItems().addAll("Deposit", "Withdraw");
        comboBoxType.getSelectionModel().select(0);

        comboBoxType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            String textEntered = textFieldAmount.getText();

            checkIfAmountEnteredIsValid(textEntered);

            if (validAmount){
                labelErrorMessage.setVisible(false);
            }else {
                labelErrorMessage.setText("Please enter a valid amount");
                labelErrorMessage.setVisible(true);
            }
        });
    }

    private void checkIfAmountEnteredIsValid(String textEntered) {
        validAmount = false;

        if(textEntered == null){
            return;
        }

        // check if value entered is only integers OR integers with 1 decimal point with 1-4 trailing digits
        if (textEntered.matches("[0-9]+|[0-9]+\\.[0-9]{1,4}")) {

            BigDecimal amountEntered = new BigDecimal(textEntered);

            // amount enterd has to be greater than 0
            if (amountEntered.compareTo(BigDecimal.ZERO) > 0) {

                // if withdraw is selected then check user balance is greater or equal to the amount entered
                validAmount = !comboBoxType.getSelectionModel().getSelectedItem().equals("Withdraw") || UserDetailsLogic.getBalance().compareTo(amountEntered) >= 0;
            } else {
                validAmount = false;
            }
        } else {
            validAmount = false;
        }
    }

    public void setMainController(InvestmentManagementViewController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void handleCancelButton(ActionEvent event) {
        mainController.removePopUp();
    }

    @FXML
    void handleConfirmButton(ActionEvent event) throws Exception{
        if (validAmount){
            BigDecimal amount = new BigDecimal(textFieldAmount.getText());

            String typeSelected = comboBoxType.getSelectionModel().getSelectedItem();

            // if withdrawing funds then negate the amount to remove it from the account
            if (typeSelected.equals("Withdraw")){
                amount = amount.negate();
            }

            UserDetailsLogic.updateBalance(amount);

        }
    }


    private void onAmountChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        checkIfAmountEnteredIsValid(newValue);

        if (validAmount) {
            labelErrorMessage.setVisible(false);
        } else {
            labelErrorMessage.setText("Please enter a valid amount");
            labelErrorMessage.setVisible(true);
        }
    }
}

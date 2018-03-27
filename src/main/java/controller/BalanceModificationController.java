package main.java.controller;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.logic.UserDetailsLogic;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

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

        initialiseComboBoxType();

        initialiseTextFieldAmount();
    }

    private void initialiseTextFieldAmount() {
        textFieldAmount.textProperty().addListener(this::onAmountChanged);
    }

    private void initialiseComboBoxType() {
        // options given to the user
        String[] options = {"Deposit", "Withdraw"};

        // add options to the combo box
        comboBoxType.getItems().addAll(options);

        // select first option
        comboBoxType.getSelectionModel().select(0);

        // on selection changed listener
        comboBoxType.getSelectionModel().selectedItemProperty().addListener(this::onOptionChanged);
    }

    /**
     * checks if user input is valid
     * @param textEntered user input
     */
    private void checkIfAmountEnteredIsValid(String textEntered) {
        validAmount = false;

        if(textEntered == null){
            return;
        }

        // check if value entered is only integers OR integers with 1 decimal point with 1-4 trailing digits
        if (textEntered.matches("[0-9]+|[0-9]+\\.[0-9]{1,4}")) {

            BigDecimal amountEntered = new BigDecimal(textEntered);

            // amount entered has to be greater than 0
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

    /**
     * add the main controller to the controller
     * @param mainController controller of the window that this view is placed in
     */
    public void setMainController(InvestmentManagementViewController mainController) {
        this.mainController = mainController;
    }

    /**
     * closes the current view when the cancel button is pressed
     * @param event
     */
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

                UserDetailsLogic.withdraw(amount);
            }else {
                UserDetailsLogic.deposit(amount);
            }

            validAmount = false;
            mainController.removePopUp();
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

    private void onOptionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String textEntered = textFieldAmount.getText();

        checkIfAmountEnteredIsValid(textEntered);

        if (validAmount) {
            labelErrorMessage.setVisible(false);
        } else {
            labelErrorMessage.setText("Please enter a valid amount");
            labelErrorMessage.setVisible(true);
        }
    }
}

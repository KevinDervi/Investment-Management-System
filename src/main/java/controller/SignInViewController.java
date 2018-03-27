package main.java.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.java.logic.SignInLogic;

public class SignInViewController {

    @FXML
    private Button signInButton;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button signUpButton;

    @FXML
    private GridPane grid;

    @FXML
    private Label errorMessage;

    @FXML
    private TextField usernameTextField;

    /**
     * determines the action taken when the user interacts with the Sign In button
     * @param event
     * @throws Exception
     */
    @FXML
    void HandleSignIn(ActionEvent event) throws Exception{
        errorMessage.setVisible(false);

        String usernameInput = usernameTextField.getText();
        String passwordInput = passwordTextField.getText();

        // determines if correct username and password has been inputted
        if (SignInLogic.Authentication(usernameInput, passwordInput)){
            SignInLogic.loadUser(usernameInput);
            // create new window
            Stage newStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/InvestmentManagementView.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            newStage.setScene(new Scene(root));
            newStage.show();

            // set minimum size of main Investment management window
            newStage.setMinWidth(newStage.getWidth());
            newStage.setMinHeight(newStage.getHeight());


            // close the current window
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();

        }
        else{
            errorMessage.setText("Wrong Username or Password");
            errorMessage.setVisible(true);
        }

    }

    /**
     * sends the user to the sign up window
     * @param event
     * @throws Exception
     */
    @FXML
    void HandleSignUp(ActionEvent event) throws Exception{
        Stage stage = (Stage) signUpButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignUpView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.sizeToScene();
    }

}


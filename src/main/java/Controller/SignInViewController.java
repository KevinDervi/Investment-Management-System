package main.java.Controller;

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
import javafx.scene.text.Text;
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
    private Text errorMessage;

    @FXML
    private TextField usernameTextField;

    @FXML
    void HandleSignIn(ActionEvent event) {
        String usernameInput = usernameTextField.getText();
        String passwordInput = passwordTextField.getText();

        if (SignInLogic.Authentication(usernameInput, passwordInput)){
            SignInLogic.loadUser(usernameInput);

        }
        else{
            errorMessage.setText("Wrong Username or Password");
        }

        // TODO get main investment management system controller from FXML Loader and call its initialise method to load properly
    }

    @FXML
    void HandleSignUp(ActionEvent event) throws Exception{
        Stage stage = (Stage) signUpButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignUpView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        stage.setScene(new Scene(root));
    }

}


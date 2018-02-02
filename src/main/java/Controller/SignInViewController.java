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
    void HandleSignIn(ActionEvent event) throws Exception{
        String usernameInput = usernameTextField.getText();
        String passwordInput = passwordTextField.getText();

        if (SignInLogic.Authentication(usernameInput, passwordInput)){
            SignInLogic.loadUser(usernameInput);
            // create new window
            Stage newStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/InvestmentManagementView.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            newStage.setScene(new Scene(root));
            newStage.show();

            // close the current window
            Stage currentStage = (Stage) signInButton.getScene().getWindow();
            currentStage.close();

        }
        else{
            errorMessage.setText("Wrong Username or Password");
        }

        // TODO get main investment management system controller from FXML Loader and call its initialise method to load properly
        // no need can populate automatically using this method in the controller and nothing has to be called
//        @FXML
//        public void initialize() {
//            System.out.println("second");
//        }


    }

    @FXML
    void HandleSignUp(ActionEvent event) throws Exception{
        Stage stage = (Stage) signUpButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignUpView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        stage.setScene(new Scene(root));
    }

}


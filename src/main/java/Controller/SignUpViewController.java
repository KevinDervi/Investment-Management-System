package main.java.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.logic.SignUpLogic;


public class SignUpViewController {

    @FXML
    private TextField reTypedPassword;

    @FXML
    private Label passwordErrorMessage;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private ImageView passwordInfo;

    @FXML
    private Label firstNameErrorMessage;

    @FXML
    private Label reTypedPasswordErrorMessage;

    @FXML
    private ImageView usernameInfo;

    @FXML
    private Label surnameErrorMessage;

    @FXML
    private Button backButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Label usernameErrorMessage;

    @FXML
    private Label emailErrorMessage;

    @FXML
    private TextField usernameTextField;

    @FXML
    void handleSignUp(ActionEvent e){
        // TODO validate all input and if valid then sign up user
        // TODO set buttons to null until validation checked
        resetErrorMessages();

        boolean validInputs = true;
        String firstName = firstNameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String retypedPassword = reTypedPassword.getText();

        // will return null if it is valid
        String firstNameMessage = firstNameGetErrorMessage(firstName);

        if(firstNameMessage != null){
            firstNameErrorMessage.setText(firstNameMessage);
            validInputs = false;
        }





        // if all inputs have been validated
        if(validInputs){
            SignUpLogic.createUser(username, password, firstName, surname, email);
            // TODO give message for successful sign up and send to sign in screen
        }

    }

    private String firstNameGetErrorMessage(String firstName) {
        return "test";
    }

    /**
     * gets the stage of the Sign up scene and replaces it with the sign in scene
     * @param e
     * @throws Exception
     */
    @FXML
    void handleBack(ActionEvent e)throws Exception{

        Stage stage = (Stage) backButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignInView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        stage.setScene(new Scene(root));
    }

    private void resetErrorMessages(){
        firstNameErrorMessage.setText("");
        surnameErrorMessage.setText("");
        emailErrorMessage.setText("");
        usernameErrorMessage.setText("");
        passwordErrorMessage.setText("");
        reTypedPasswordErrorMessage.setText("");
    }

}

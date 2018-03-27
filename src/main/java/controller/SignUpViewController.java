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
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.java.logic.SignUpLogic;


public class SignUpViewController {

    @FXML
    private PasswordField reTypedPasswordTextField;

    @FXML
    private Label passwordErrorMessage;

    @FXML
    private PasswordField passwordTextField;

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

    /**
     * validates user input and insert into database upon successful validation
     * @param event
     */
    @FXML
    void handleSignUp(ActionEvent event){

        resetErrorMessages();

        boolean validInputs = true;

        String firstName = firstNameTextField.getText();
        String surname = surnameTextField.getText();
        String email = emailTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String retypedPassword = reTypedPasswordTextField.getText();

        // first name validation
        String firstNameMessage = SignUpLogic.getGenericTextErrorMessage(firstName);

        if(firstNameMessage != null){
            firstNameErrorMessage.setText(firstNameMessage);
            firstNameErrorMessage.setVisible(true);
            validInputs = false;
        }

        //surname validation
        String surnameMessage = SignUpLogic.getGenericTextErrorMessage(surname);

        if(surnameMessage != null){
            surnameErrorMessage.setText(surnameMessage);
            surnameErrorMessage.setVisible(true);
            validInputs = false;
        }

        // E-mail validation
        String emailMessage = SignUpLogic.getEmailErrorMessage(email);

        if(emailMessage != null){
            emailErrorMessage.setText(emailMessage);
            emailErrorMessage.setVisible(true);
            validInputs = false;
        }

        // username validation
        String usernameMessage = SignUpLogic.getUsernameErrorMessage(username);

        if(usernameMessage != null){
            usernameErrorMessage.setText(usernameMessage);
            usernameErrorMessage.setVisible(true);
            validInputs = false;

        }

        //password validation
        String passwordMessage = SignUpLogic.getPasswordErrorMessage(password);

        if(passwordMessage != null){
            passwordErrorMessage.setText(passwordMessage);
            passwordErrorMessage.setVisible(true);
            validInputs = false;
        }

        //re-typed password validation
        String reTypedPasswordMessage = SignUpLogic.getReTypedPasswordMessage(password, retypedPassword);

        if(reTypedPasswordMessage != null){
            reTypedPasswordErrorMessage.setText(reTypedPasswordMessage);
            reTypedPasswordErrorMessage.setVisible(true);
            validInputs = false;

        }

        // if all inputs have been validated
        if(validInputs){
            System.out.println("all Sign Up inputs are valid");
            SignUpLogic.createUser(username, password, firstName, surname, email);

            try {
                handleBack(null);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * sends the user back to the Sign In screen
     * @param e
     * @throws Exception
     */
    @FXML
    void handleBack(ActionEvent e)throws Exception{

        Stage stage = (Stage) backButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignInView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.sizeToScene();
    }

    /**
     * resets and hides all the error messages
     */
    private void resetErrorMessages(){
        firstNameErrorMessage.setText("");
        firstNameErrorMessage.setVisible(false);

        surnameErrorMessage.setText("");
        surnameErrorMessage.setVisible(false);

        emailErrorMessage.setText("");
        emailErrorMessage.setVisible(false);

        usernameErrorMessage.setText("");
        usernameErrorMessage.setVisible(false);

        passwordErrorMessage.setText("");
        passwordErrorMessage.setVisible(false);

        reTypedPasswordErrorMessage.setText("");
        reTypedPasswordErrorMessage.setVisible(false);
    }

}

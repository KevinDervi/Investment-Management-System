package main.java.Controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import main.java.logic.SignInLogic;

public class SignIn extends Application {

    TextField usernameTextField;
    PasswordField passwordTextField;
    TextField errorMessage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Investment Management System");

        GridPane grid = new GridPane(); //grid is used for forms due to easy formatting
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // horizontal gap
        grid.setVgap(10); // vertical gap
        grid.setPadding(new Insets(25, 25, 25, 25)); // padding the sides of the grid

        Scene scene = new Scene(grid, Color.BLUE);


        ////////////
        // title at top of form
        Label signUpTitle = new Label("Sign In");
        signUpTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
        GridPane.setHalignment(signUpTitle, HPos.CENTER); //aligns title with center (setAlignment() will not work)
        grid.add(signUpTitle, 0, 0, 2, 1);

        //error message
        errorMessage = new TextField();
        grid.add(errorMessage, 0, 1);
        errorMessage.setVisible(false);

        // username
        Label username = new Label("Username:");
        grid.add(username, 0, 2);

        usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 2);

        // password
        Label password = new Label("Password:");
        grid.add(password, 0, 3);

        passwordTextField = new PasswordField();
        grid.add(passwordTextField, 1, 3);
        ////////////////


        Button signInButton = new Button("Sign In");
        Button SignUpButton = new Button("Sign Up");

        VBox buttonRegion = new VBox(10);
        buttonRegion.setAlignment(Pos.CENTER);
        buttonRegion.getChildren().addAll(signInButton, SignUpButton);

        grid.add(buttonRegion, 0, 7, 2, 1);


        //sign in button functionality
        signInButton.setOnAction(this::handleSignIn);

        //sign up button functionality

        // window config
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSignIn(ActionEvent e){
        //TODO perform checks and add user to database

        String usernameInput = usernameTextField.getText();
        String passwordInput = passwordTextField.getText();

        SignInLogic.Authentication(usernameInput, passwordInput);
    }

    public void DisplayErrorMessage(String message){
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }
}

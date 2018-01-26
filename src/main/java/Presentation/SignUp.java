package main.java.Presentation;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.java.logic.CreateUser;

public class SignUp extends Application {

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
        Label signUpTitle = new Label("Sign Up");
        signUpTitle.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 20));
        GridPane.setHalignment(signUpTitle, HPos.CENTER); //aligns title with center (setAlignment() will not work)
        grid.add(signUpTitle, 0, 0, 2, 1);

        // first name
        Label firstName = new Label("First Name:");
        firstName.setAlignment(Pos.CENTER_RIGHT);
        grid.add(firstName, 0, 1);

        TextField firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 1);

        // surname
        Label surname = new Label("Surname:");
        grid.add(surname, 0, 2);

        TextField surnameTextField = new TextField();
        grid.add(surnameTextField, 1, 2);

        // email
        Label email = new Label("E-mail:");
        grid.add(email, 0, 3);

        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 3);

        // username
        Label username = new Label("Username:");
        grid.add(username, 0, 4);

        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 4);

        // password
        Label password = new Label("Password:");
        grid.add(password, 0, 5);

        PasswordField passwordTextField = new PasswordField();
        grid.add(passwordTextField, 1, 5);

        // second re-typed password field for security
        Label passwordCheck = new Label("Re-type Password:");
        grid.add(passwordCheck, 0, 6);

        PasswordField passwordCheckTextField = new PasswordField();
        grid.add(passwordCheckTextField, 1, 6);
        ////////////////


        Button signUpButton = new Button("Sign up");
        Button backButton = new Button("Back");

        VBox buttonRegion = new VBox(10);
        buttonRegion.setAlignment(Pos.CENTER);
        buttonRegion.getChildren().addAll(signUpButton, backButton);

        grid.add(buttonRegion, 0, 7, 2, 1);

        //////////////////////

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //TODO perform checks and add user to database

                String usernameInput = usernameTextField.getText();
                String passwordInput = passwordTextField.getText();
                String firstNameInput = firstNameTextField.getText();
                String surnameInput = surnameTextField.getText();
                String emailInput = emailTextField.getText();

                CreateUser.createUser(usernameInput, passwordInput, firstNameInput, surnameInput, emailInput);
            }
        });

        // window config
        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

package main.java.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    public static void main(String[] args){launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        // load initial screen to show user (Sign In Screen)
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resources/views/SignInView.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        primaryStage.setTitle("Investment Management System");

        // add the scene to the primary stage
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        // make the sign in screen non resizable
        primaryStage.setResizable(false);
    }
}

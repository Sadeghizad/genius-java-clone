package com.genius.ui;

import com.genius.data.DataStore;
import com.genius.data.FileHandler;
import com.genius.service.SeedDataService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class Main extends Application {
    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void start(Stage stage) {
        Label welcome = new Label("Welcome to Genius App");
        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Signup");
        welcome.setStyle("-fx-text-fill:white; ;");
        signupBtn.setStyle("-fx-background-color: #32cd32;");
        loginBtn.setStyle("-fx-background-color: #00bfff;");

        loginBtn.setOnAction(e -> LoginScreen.show(stage));
        signupBtn.setOnAction(e -> SignupScreen.show(stage));

        VBox layout = new VBox(15, welcome, loginBtn, signupBtn);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center;-fx-background-color: #292929;-fx-border-color: white;-fx-border-style: solid;-fx-border-width: 2;-fx-border-insets: 0;-fx-border-radius: 2;");

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Genius App");
        stage.setScene(scene);
        stage.show();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileHandler.saveDataStore();  // Save data to file when the application shuts down
            logger.info("Data saved successfully on exit.");
        }));
    }

    public static void main(String[] args) {
        FileHandler.loadDataStore();
        if (DataStore.accounts.isEmpty()) {
            SeedDataService.generate();
        }
        launch(args);
    }
}

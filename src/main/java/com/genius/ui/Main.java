package com.genius.ui;

import com.genius.data.DataStore;
import com.genius.service.SeedDataService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        if (DataStore.accounts.isEmpty()) {
            SeedDataService.generate();
        }

        Label welcome = new Label("Welcome to Genius App");
        Button loginBtn = new Button("Login");
        Button signupBtn = new Button("Signup");

        loginBtn.setOnAction(e -> LoginScreen.show(stage));
        signupBtn.setOnAction(e -> SignupScreen.show(stage));

        VBox layout = new VBox(15, welcome, loginBtn, signupBtn);
        layout.setStyle("-fx-padding: 30; -fx-alignment: center");

        Scene scene = new Scene(layout, 400, 300);
        stage.setTitle("Genius App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

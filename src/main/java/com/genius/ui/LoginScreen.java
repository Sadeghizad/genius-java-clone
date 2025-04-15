package com.genius.ui;

import com.genius.model.Account;
import com.genius.model.Admin;
import com.genius.model.Artist;
import com.genius.model.User;
import com.genius.service.AccountService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    public static void show(Stage stage) {
        Label label = new Label("Login");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginBtn = new Button("Login");
        Label message = new Label();

        loginBtn.setOnAction(e -> {
            try {
                message.setText("");
                Account acc = AccountService.login(usernameField.getText(), passwordField.getText());
                if (acc instanceof User user) {
                    UserStage.show(stage, user);
                } else if (acc instanceof Artist artist) {
                    ArtistStage.show(stage, artist);
                } else if (acc instanceof Admin admin) {
                    AdminStage.show(stage, admin);
                }else{
                    System.out.println("Unknown account type: " + acc.getClass().getSimpleName());
                    message.setText("Login failed: Unknown account type.");
                }
            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> new Main().start(stage));
        VBox layout = new VBox(10, label, usernameField, passwordField, loginBtn, message, back);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #e7ce55;");

        stage.setScene(new Scene(layout, 400, 300));
    }
}

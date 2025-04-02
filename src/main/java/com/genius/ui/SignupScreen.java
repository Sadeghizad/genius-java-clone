package com.genius.ui;

import com.genius.model.Account;
import com.genius.model.User;
import com.genius.model.Artist;
import com.genius.service.AccountService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SignupScreen {
    public static void show(Stage stage) {
        Label label = new Label("Signup");
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        TextField ageField = new TextField(); ageField.setPromptText("Age");
        TextField emailField = new TextField(); emailField.setPromptText("Email");
        TextField usernameField = new TextField(); usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField(); passwordField.setPromptText("Password");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("user", "artist");
        roleBox.setPromptText("Select Role");

        Button signupBtn = new Button("Signup");
        Label message = new Label();

        signupBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String email = emailField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String role = roleBox.getValue();

                Account acc = AccountService.signup(name, age, email, username, password, role);

                if (acc instanceof User user) {
                    UserStage.show(stage, user);
                } else if (acc instanceof Artist artist) {
                    ArtistStage.show(stage, artist);
                }
            } catch (Exception ex) {
                message.setText(ex.getMessage());
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> new Main().start(stage));
        VBox layout = new VBox(10, label, nameField, ageField, emailField, usernameField, passwordField, roleBox, signupBtn, message,back);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");

        stage.setScene(new Scene(layout, 400, 450));
    }
}

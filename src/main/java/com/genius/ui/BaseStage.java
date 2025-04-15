package com.genius.ui;

import javafx.scene.control.Alert;

public class BaseStage {
    static void showText(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

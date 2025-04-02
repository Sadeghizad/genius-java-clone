package com.genius.ui;

import com.genius.model.*;
import com.genius.data.DataStore;
import com.genius.service.EditService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminStage {

    public static void show(Stage stage, Admin admin) {
        Label welcome = new Label("Welcome Admin: " + admin.getName());

        Button reviewEdits = new Button("Review All Pending Lyric Edits");
        Button logout = new Button("Logout");
        Button approveArtists = new Button("Approve Pending Artists");
        Button manageAccounts = new Button("Manage Accounts");

        reviewEdits.setOnAction(e -> {
            var edits = EditService.getAllPendingEdits();

            for (LyricEdit edit : edits) {
                Song s = DataStore.songs.get(edit.getSongId());
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Edit for Song: " + (s != null ? s.getTitle() : edit.getSongId()));
                alert.setContentText("Suggested by: " + edit.getSuggestedByUsername() + "\n\n" + edit.getSuggestedLyrics());
                ButtonType approve = new ButtonType("Approve");
                ButtonType reject = new ButtonType("Reject");
                alert.getButtonTypes().setAll(approve, reject, ButtonType.CANCEL);

                var result = alert.showAndWait();
                if (result.isPresent()) {
                    if (result.get() == approve) EditService.approveEdit(edit);
                    else if (result.get() == reject) EditService.rejectEdit(edit);
                }
            }
        });

        logout.setOnAction(e -> new Main().start(stage));

        approveArtists.setOnAction(e -> {
            var unapproved = DataStore.accounts.values().stream()
                    .filter(a -> a instanceof Artist artist && !artist.isApproved())
                    .map(a -> (Artist) a).toList();

            for (Artist a : unapproved) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Approve Artist: " + a.getUsername());
                alert.setContentText(a.getName() + "\nEmail: " + a.getEmail());
                alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                var result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    a.approve();
                }
            }
        });

        manageAccounts.setOnAction(e -> {
            ListView<Account> list = new ListView<>();
            list.getItems().addAll(DataStore.accounts.values());
            list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            list.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Account item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" :
                            "[" + item.getClass().getSimpleName() + "] " +
                                    item.getUsername() + " (" + item.getName() + ")");
                }
            });
            ComboBox<String> filter = new ComboBox<>();
            filter.getItems().addAll("All", "User", "Artist", "Admin");
            filter.setOnAction(f -> {
                String selected = filter.getValue();
                list.getItems().clear();
                DataStore.accounts.values().forEach(acc -> {
                    if (selected.equals("All") || acc.getClass().getSimpleName().equalsIgnoreCase(selected)) {
                        list.getItems().add(acc);
                    }
                });
            });

            Stage subStage = getSubStage(filter, list);
            subStage.show();
        });


        VBox layout = new VBox(10, welcome, reviewEdits,approveArtists,manageAccounts, logout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");

        stage.setScene(new Scene(layout, 450, 400));
    }

    private static Stage getSubStage(ComboBox<String> filter, ListView<Account> list) {
        Button delete = new Button("Delete Selected");
        delete.setOnAction(d -> {
            var selected = list.getSelectionModel().getSelectedItems();
            selected.forEach(acc -> DataStore.accounts.remove(acc.getUsername()));
            list.getItems().removeAll(selected);
        });

        VBox box = new VBox(10,
                new Label("Filter by Type:"), filter,
                new Label("All Accounts:"), list, delete
        );
        box.setStyle("-fx-padding: 20");

        Stage subStage = new Stage();
        subStage.setScene(new Scene(box, 500, 550));
        subStage.setTitle("Manage Accounts");
        return subStage;
    }

    private static void show(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

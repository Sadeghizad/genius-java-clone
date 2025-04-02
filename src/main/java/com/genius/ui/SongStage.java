package com.genius.ui;

import com.genius.model.Song;
import com.genius.model.User;
import com.genius.service.CommentService;
import com.genius.service.EditService;
import com.genius.service.SongService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SongStage {

    public static void show(Stage stage, Song song, User user) {
        SongService.incrementView(song.getId());

        Label title = new Label("Title: " + song.getTitle());
        Label lyrics = new Label("Lyrics:\n" + song.getLyrics());
        Label views = new Label("Views: " + song.getViewCount());
        Label artists = new Label("Artists: " + song.getArtistUsernames());

        Button suggestEdit = new Button("Suggest Edit");
        Button commentBtn = new Button("Comment");
        Button backBtn = new Button("Back");

        suggestEdit.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Suggest new lyrics:");
            dialog.showAndWait().ifPresent(input ->
                    EditService.suggestEdit(song.getId(), input, user.getUsername()));
        });

        commentBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("Enter your comment:");
            dialog.showAndWait().ifPresent(c -> {
                CommentService.addComment(user, c, song);
            });
        });

        backBtn.setOnAction(e -> UserStage.show(stage, user));

        VBox layout = new VBox(10, title, artists, lyrics, views, suggestEdit, commentBtn, backBtn);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center-left");

        stage.setScene(new Scene(layout, 500, 500));
    }
}

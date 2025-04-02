package com.genius.ui;

import com.genius.model.Album;
import com.genius.model.Song;
import com.genius.model.User;
import com.genius.service.AlbumService;
import com.genius.service.SongService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlbumStage {
    public static void show(Stage stage, Album album, User user) {
        Label title = new Label("Title: " + album.getTitle());
        Label date = new Label("Release Date: " + album.getReleaseDate());
        Label artist = new Label("Artist: " + AlbumService.getArtist(album.getId()));

        ListView<Song> tracklist = new ListView<>();
        for (String songId : album.getSongIds()) {
            Song song = SongService.getSongById(songId);
            if (song != null) tracklist.getItems().add(song);
        }

        tracklist.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Song item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getTitle());
            }
        });

        tracklist.setOnMouseClicked(ev -> {
            Song selected = tracklist.getSelectionModel().getSelectedItem();
            if (selected != null) {
                SongStage.show(stage, selected, user);
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> UserStage.show(stage, user));

        VBox layout = new VBox(10, title, artist, date, new Label("Tracklist:"), tracklist, back);
        layout.setStyle("-fx-padding: 20");

        stage.setScene(new Scene(layout, 500, 600));
    }
}

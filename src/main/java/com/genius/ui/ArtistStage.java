package com.genius.ui;

import com.genius.model.Artist;
import com.genius.model.Song;
import com.genius.service.*;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ArtistStage {
    public static void show(Stage stage, Artist artist) {
        Label welcome = new Label("Welcome, " + artist.getName());

        Button createSong = new Button("Create New Song");
        Button createAlbum = new Button("Create New Album");
        Button approveEdits = new Button("Review Lyric Edits");
        Button viewProfile = new Button("View My Songs and Albums");
        Button logout = new Button("Logout");

        createSong.setOnAction(e -> {
            TextInputDialog titleDialog = new TextInputDialog();
            titleDialog.setHeaderText("Enter song title:");
            titleDialog.showAndWait().ifPresent(title -> {
                TextInputDialog lyricsDialog = new TextInputDialog();
                lyricsDialog.setHeaderText("Enter lyrics:");
                lyricsDialog.showAndWait().ifPresent(lyrics -> {
                    SongService.createSong(title, lyrics, "unknown", List.of(), LocalDate.now(), null, List.of(artist.getUsername()));
                    show("Done", "Song created successfully.");
                });
            });
        });

        createAlbum.setOnAction(e -> {
            TextInputDialog titleDialog = new TextInputDialog();
            titleDialog.setHeaderText("Enter album title:");
            titleDialog.showAndWait().ifPresent(title -> {
                var album = AlbumService.createAlbum(title, LocalDate.now(), artist);
                show("Done", "Album created.\nID: " + album.getId());
            });
        });

        approveEdits.setOnAction(e -> {
            var edits = EditService.getAllPendingEdits().stream()
                    .filter(m -> artist.getSongIds().contains(m.getSongId()))
                    .toList();

            for (var edit : edits) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Edit for Song ID: " + edit.getSongId());
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

        viewProfile.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("My Songs:\n");
            for (String id : artist.getSongIds()) {
                Song s = SongService.getSongById(id);
                if (s != null) sb.append("[").append(s.getId()).append("] ").append(s.getTitle()).append("\n");
            }
            sb.append("\nMy Albums:\n");
            for (String id : artist.getAlbumIds()) {
                var album = AlbumService.getAlbumById(id);
                if (album != null) sb.append("[").append(album.getId()).append("] ").append(album.getTitle()).append("\n");
            }
            show("Your Profile", sb.toString());
        });

        logout.setOnAction(e -> new Main().start(stage));

        VBox layout = new VBox(10, welcome, createSong, createAlbum, approveEdits, viewProfile, logout);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center");
        stage.setScene(new Scene(layout, 500, 500));
    }

    private static void show(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
